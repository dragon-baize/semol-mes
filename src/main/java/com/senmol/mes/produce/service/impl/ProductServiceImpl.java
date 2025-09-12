package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.produce.entity.BomEntity;
import com.senmol.mes.produce.entity.ProductEntity;
import com.senmol.mes.produce.mapper.ProductMapper;
import com.senmol.mes.produce.service.BomService;
import com.senmol.mes.produce.service.ProductService;
import com.senmol.mes.produce.utils.ProAsyncUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.LineVo;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.service.StorageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品管理(Product)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductEntity> implements ProductService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Lazy
    @Resource
    private ProAsyncUtil proAsyncUtil;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private RedisService redisService;
    @Resource
    private BomService bomService;
    @Resource
    private StorageService storageService;
    @Resource
    private ProduceService produceService;

    @Override
    public List<ProductEntity> getList(Integer source, Long bomId, Long supplierId) {
        return this.baseMapper.getList(source, bomId, supplierId);
    }

    @Override
    public SaResult selectAll(Page<ProductEntity> page, ProductEntity product) {
        this.page(page, new QueryWrapper<>(product));
        List<ProductEntity> records = page.getRecords();

        for (ProductEntity record : records) {
            LineVo line = this.proFromRedis.getLine(record.getProductLineId());
            if (ObjUtil.isNotNull(line)) {
                record.setProductLineCode(line.getCode());
                record.setProductLineTitle(line.getTitle());
            }

            record.setUnitTitle(this.sysFromRedis.getDictMx(record.getUnitId()));
            record.setStatusTitle(this.sysFromRedis.getDictMx(record.getStatus()));
        }

        return SaResult.data(page);
    }

    @Override
    public SaResult selectOne(Long id) {
        ProductEntity product = this.getById(id);
        product.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
        return SaResult.data(product);
    }

    @Override
    public SaResult insertProduct(ProductEntity product) {
        long l = CheckToolUtil.checkCodeExist(this, null, product.getCode());
        if (l > 0L) {
            return SaResult.error("产品编号重复");
        }

        // 保存产品数据
        this.save(product);
        // 添加到缓存
        this.proAsyncUtil.dealProduct(product);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateProduct(ProductEntity product) {
        long l = CheckToolUtil.checkCodeExist(this, product.getId(), product.getCode());
        if (l > 0L) {
            return SaResult.error("产品编号重复");
        }

        // 更新产品数据
        this.updateById(product);
        // 添加到缓存
        this.proAsyncUtil.dealProduct(product);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteProduct(Long id) {
        Long count = this.bomService.lambdaQuery()
                .eq(BomEntity::getProductId, id)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("产品已绑定清单");
        }

        count = this.storageService.lambdaQuery()
                .eq(StorageEntity::getGoodsId, id)
                .gt(StorageEntity::getStatus, 0)
                .gt(StorageEntity::getResidueQty, 0)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("产品存在库存数据");
        }

        boolean b = this.removeById(id);
        if (b) {
            // 删除缓存
            this.redisService.del(RedisKeyEnum.PRODUCE_PRODUCT.getKey() + id);
            this.produceService.lambdaUpdate()
                    .set(ProduceEntity::getExist, 0)
                    .eq(ProduceEntity::getProductId, id)
                    .update();
        }
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

