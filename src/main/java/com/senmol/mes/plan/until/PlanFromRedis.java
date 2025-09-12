package com.senmol.mes.plan.until;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.plan.entity.CustomEntity;
import com.senmol.mes.plan.entity.Supplier;
import com.senmol.mes.plan.entity.SupplierGoods;
import com.senmol.mes.plan.service.CustomService;
import com.senmol.mes.plan.service.SupplierGoodsService;
import com.senmol.mes.plan.service.SupplierService;
import com.senmol.mes.plan.vo.SupplierGoodsVo;
import com.senmol.mes.plan.vo.SupplierVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Component
public class PlanFromRedis {

    @Resource
    private RedisService redisService;
    @Resource
    private CustomService customService;
    @Resource
    private SupplierGoodsService supplierGoodsService;
    @Resource
    private SupplierService supplierService;

    /**
     * 获取客户
     */
    public String getCustom(Long customId) {
        if (ObjectUtil.isNull(customId)) {
            return "";
        }

        Object object = this.redisService.get(RedisKeyEnum.PLAN_CUSTOM.getKey() + customId);
        if (ObjectUtil.isNull(object)) {
            CustomEntity custom = this.customService.getById(customId);
            if (ObjectUtil.isNull(custom)) {
                return "";
            } else {
                object = custom.getTitle();
            }
        }

        this.redisService.set(RedisKeyEnum.PLAN_CUSTOM.getKey() + customId, object,
                RedisKeyEnum.PLAN_CUSTOM.getTimeout());

        return object.toString();
    }

    /**
     * 获取供应商
     */
    public SupplierVo getSupplier(Long supplierId) {
        if (ObjUtil.isNull(supplierId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PLAN_SUPPLIER.getKey() + supplierId);
        if (ObjectUtil.isNull(object)) {
            Supplier supplier = this.supplierService.getById(supplierId);
            if (ObjUtil.isNull(supplier)) {
                return null;
            }

            object = Convert.convert(SupplierVo.class, supplier);
        }

        this.redisService.set(RedisKeyEnum.PLAN_SUPPLIER.getKey() + supplierId, object,
                RedisKeyEnum.PLAN_SUPPLIER.getTimeout());

        return (SupplierVo) object;
    }

    /**
     * 供应商物料数据
     */
    public SupplierGoodsVo getSupplierMaterial(Long materialId) {
        if (ObjUtil.isNull(materialId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PLAN_MATERIAL.getKey() + materialId);
        if (ObjectUtil.isNull(object)) {
            SupplierGoods supplierGoods = this.supplierGoodsService.lambdaQuery()
                    .eq(SupplierGoods::getGoodsId, materialId)
                    .last(CheckToolUtil.LIMIT)
                    .one();

            object = Convert.convert(SupplierGoodsVo.class, supplierGoods);
        }


        this.redisService.set(RedisKeyEnum.PLAN_MATERIAL.getKey() + materialId, object,
                RedisKeyEnum.PLAN_MATERIAL.getTimeout());

        return (SupplierGoodsVo) object;
    }

}
