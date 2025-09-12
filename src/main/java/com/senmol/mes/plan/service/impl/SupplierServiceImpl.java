package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.plan.entity.Supplier;
import com.senmol.mes.plan.entity.SupplierGoods;
import com.senmol.mes.plan.mapper.SupplierMapper;
import com.senmol.mes.plan.service.SupplierGoodsService;
import com.senmol.mes.plan.service.SupplierService;
import com.senmol.mes.plan.until.PlanAsyncUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商管理(PlanSupplier)表服务实现类
 *
 * @author makejava
 * @since 2024-05-16 13:27:35
 */
@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Resource
    private SupplierGoodsService supplierGoodsService;
    @Resource
    private PlanAsyncUtil planAsyncUtil;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private RedisService redisService;

    @Override
    public SaResult selectById(Long id) {
        Supplier supplier = this.getById(id);

        List<SupplierGoods> goods = this.supplierGoodsService.lambdaQuery()
                .eq(SupplierGoods::getPid, id)
                .list();
        for (SupplierGoods good : goods) {
            if (good.getType() == 1) {
                ProductVo product = this.proFromRedis.getProduct(good.getGoodsId());
                if (ObjUtil.isNotNull(product)) {
                    good.setGoodsCode(product.getCode());
                    good.setGoodsTitle(product.getTitle());
                }
            } else {
                MaterialVo material = this.proFromRedis.getMaterial(good.getGoodsId());
                if (ObjUtil.isNotNull(material)) {
                    good.setGoodsCode(material.getCode());
                    good.setGoodsTitle(material.getTitle());
                }
            }
        }

        supplier.setGoods(goods);
        return SaResult.data(supplier);
    }

    @Override
    public SaResult selectPage(Page<Supplier> page, Supplier supplier) {
        Page<Supplier> result = this.page(page, new QueryWrapper<>(supplier));
        List<Supplier> records = result.getRecords();
        records.forEach(item -> item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser())));
        return SaResult.data(result);
    }

    @Override
    public SaResult insert(Supplier supplier) {
        this.checkCodeAndName(supplier);
        this.save(supplier);

        Long id = supplier.getId();
        List<SupplierGoods> goods = supplier.getGoods();

        goods.forEach(item -> item.setPid(id));
        this.supplierGoodsService.saveBatch(goods);

        this.planAsyncUtil.delSupplierGoods(supplier, goods);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult modify(Supplier supplier) {
        this.checkCodeAndName(supplier);
        Long id = supplier.getId();
        this.supplierGoodsService.lambdaUpdate()
                .eq(SupplierGoods::getPid, id)
                .remove();

        this.updateById(supplier);

        List<SupplierGoods> goods = supplier.getGoods();
        goods.forEach(item -> item.setPid(id));
        this.supplierGoodsService.saveBatch(goods);

        this.planAsyncUtil.delSupplierGoods(supplier, goods);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult delete(Long id) {
        this.supplierGoodsService.lambdaUpdate().eq(SupplierGoods::getPid, id).remove();
        this.redisService.del(RedisKeyEnum.PLAN_SUPPLIER.getKey() + id);
        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private void checkCodeAndName(Supplier supplier) {
        Long id = supplier.getId();
        String code = supplier.getCode();
        String name = supplier.getName();
        List<Supplier> list = this.lambdaQuery().eq(Supplier::getCode, code).or().eq(Supplier::getName, name).list();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        List<Supplier> collect1 = list.stream().filter(item -> {
            String itemName = item.getName();
            return ObjUtil.isNull(id) ? itemName.equals(name) : itemName.equals(name) && !item.getId().equals(id);
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect1)) {
            throw new BusinessException("供应商名称重复");
        }

        List<Supplier> collect2 = list.stream().filter(item -> {
            String itemCode = item.getCode();
            return ObjUtil.isNull(id) ? itemCode.equals(code) : itemCode.equals(code) && !item.getId().equals(id);
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect2)) {
            throw new BusinessException("供应商编号重复");

        }
    }

}

