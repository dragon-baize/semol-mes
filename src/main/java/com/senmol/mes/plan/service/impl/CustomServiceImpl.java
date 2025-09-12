package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.plan.entity.CustomEntity;
import com.senmol.mes.plan.entity.CustomProductEntity;
import com.senmol.mes.plan.entity.SaleOrderEntity;
import com.senmol.mes.plan.mapper.CustomMapper;
import com.senmol.mes.plan.service.CustomProductService;
import com.senmol.mes.plan.service.CustomService;
import com.senmol.mes.plan.service.SaleOrderService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.BomVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 客户管理(Custom)表服务实现类
 *
 * @author makejava
 * @since 2023-07-13 16:18:45
 */
@Service("customService")
public class CustomServiceImpl extends ServiceImpl<CustomMapper, CustomEntity> implements CustomService {

    @Resource
    private CustomProductService customProductService;
    @Resource
    private RedisService redisService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SaleOrderService saleOrderService;
    @Resource
    private SysFromRedis sysFromRedis;

    @Override
    public CustomEntity selectOne(Long id) {
        CustomEntity custom = this.getById(id);

        List<CustomProductEntity> list
                = this.customProductService.lambdaQuery().eq(CustomProductEntity::getCustomId, id).list();
        list.forEach(item -> {
            if (ObjectUtil.isNotNull(item.getProductId())) {
                ProductVo product = this.proFromRedis.getProduct(item.getProductId());
                if (ObjUtil.isNotNull(product)) {
                    item.setProductCode(product.getCode());
                    item.setProductTitle(product.getTitle());
                }
            }
        });

        custom.setCustomProducts(list);
        return custom;
    }

    @Override
    public Page<CustomEntity> selectAll(Page<CustomEntity> page, CustomEntity custom) {
        this.page(page, new QueryWrapper<>(custom));
        List<CustomEntity> records = page.getRecords();
        records.forEach(item -> {
            item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser()));
            item.setUpdateUserName(this.sysFromRedis.getUser(item.getUpdateUser()));
        });

        return page;
    }

    @Override
    public List<CustomEntity> getByProductId(Long productId) {
        return this.baseMapper.getByProductId(productId);
    }

    @Override
    public SaResult insertCustom(CustomEntity custom) {
        String s = this.checkCodeExist(custom, custom.getCustomProducts());
        if (StrUtil.isNotBlank(s)) {
            return SaResult.error(s);
        }

        this.save(custom);

        Long id = custom.getId();
        this.redisService.set(RedisKeyEnum.PLAN_CUSTOM.getKey() + id, custom.getTitle(),
                RedisKeyEnum.PLAN_CUSTOM.getTimeout());

        List<CustomProductEntity> list = custom.getCustomProducts();
        list.forEach(item -> {
            item.setCustomId(id);
            BigDecimal divide = item.getTaxRate().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            item.setTaxPrice(item.getPrice().multiply(divide.add(BigDecimal.ONE)));
        });

        this.customProductService.saveBatch(list);

        return SaResult.data(custom);
    }

    @Override
    public SaResult updateCustom(CustomEntity custom) {
        String s = this.checkCodeExist(custom, custom.getCustomProducts());
        if (StrUtil.isNotBlank(s)) {
            return SaResult.error(s);
        }

        this.updateById(custom);

        Long id = custom.getId();
        this.redisService.set(RedisKeyEnum.PLAN_CUSTOM.getKey() + id, custom.getTitle(),
                RedisKeyEnum.PLAN_CUSTOM.getTimeout());

        this.customProductService.lambdaUpdate().eq(CustomProductEntity::getCustomId, id).remove();

        List<CustomProductEntity> list = custom.getCustomProducts();
        list.forEach(item -> {
            item.setCustomId(id);
            BigDecimal divide = item.getTaxRate().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            item.setTaxPrice(item.getPrice().multiply(divide.add(BigDecimal.ONE)));
        });

        this.customProductService.saveBatch(list);

        return SaResult.ok(custom.getId().toString());
    }

    @Override
    public SaResult deleteCustom(Long id) {
        long count = this.saleOrderService.lambdaQuery()
                .eq(SaleOrderEntity::getCustomId, id)
                .last(CheckToolUtil.LIMIT)
                .count();

        if (count > 0L) {
            return SaResult.error("客户已绑定销售订单");
        }

        this.removeById(id);

        this.customProductService.lambdaUpdate().eq(CustomProductEntity::getCustomId, id).remove();
        // 删除缓存
        this.redisService.del(RedisKeyEnum.PLAN_CUSTOM.getKey() + id);

        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    /**
     * 校验客户编号是否重复
     */
    private String checkCodeExist(CustomEntity custom, List<CustomProductEntity> entities) {
        LambdaQueryWrapper<CustomEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomEntity::getTitle, custom.getTitle());

        Long id = custom.getId();
        if (ObjUtil.isNotNull(id)) {
            wrapper.ne(CustomEntity::getId, id);
        }

        wrapper.last(CheckToolUtil.LIMIT);
        long count = this.count(wrapper);
        if (count > 0) {
            return "客户名称重复";
        }

        /*List<Long> productIds = custom.getCustomProducts()
                .stream()
                .map(CustomProductEntity::getProductId)
                .collect(Collectors.toList());

        count = this.customProductService.lambdaQuery()
                .ne(CustomProductEntity::getCustomId, custom.getId())
                .in(CustomProductEntity::getProductId, productIds)
                .count();
        if (count > 0L) {
            return "存在已被其他客户绑定的产品";
        }*/

        for (CustomProductEntity entity : entities) {
            BomVo bomVo = this.proFromRedis.getBom(entity.getProductId());
            if (ObjUtil.isNull(bomVo) || bomVo.getStatus() != 0) {
                return "产品【" + entity.getProductCode() + "】清单不存在或清单未审批通过";
            }
        }

        return null;
    }
}

