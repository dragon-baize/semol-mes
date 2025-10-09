package com.senmol.mes.plan.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.plan.entity.SaleOrderEntity;
import com.senmol.mes.plan.entity.SaleOrderProductEntity;
import com.senmol.mes.plan.mapper.SaleOrderProductMapper;
import com.senmol.mes.plan.service.SaleOrderProductService;
import com.senmol.mes.plan.service.SaleOrderService;
import com.senmol.mes.plan.vo.ProductQty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 销售订单发货(SaleOrderProduct)表服务实现类
 *
 * @author makejava
 * @since 2023-07-18 09:15:46
 */
@Service("saleOrderProductService")
public class SaleOrderProductServiceImpl extends ServiceImpl<SaleOrderProductMapper, SaleOrderProductEntity> implements SaleOrderProductService {

    @Resource
    private SaleOrderService saleOrderService;

    @Override
    public void modifyBatchById(List<SaleOrderProductEntity> products, String saleOrderCode) {
        SaleOrderEntity saleOrder = this.saleOrderService.lambdaQuery().eq(SaleOrderEntity::getCode, saleOrderCode)
                .last(CheckToolUtil.LIMIT).one();
        if (ObjUtil.isNotNull(saleOrder)) {
            this.baseMapper.modifyBatchById(products, saleOrder.getId());
        }
    }

    @Override
    public void modifyBatchById2(Map<Long, BigDecimal> map, String saleOrderCode) {
        SaleOrderEntity saleOrder = this.saleOrderService.lambdaQuery().eq(SaleOrderEntity::getCode, saleOrderCode)
                .last(CheckToolUtil.LIMIT).one();
        if (ObjUtil.isNotNull(saleOrder)) {
            this.baseMapper.modifyBatchById2(map, saleOrder.getId());
        }
    }

    @Override
    public List<ProductQty> getSumQty(List<Long> productIds, Long saleOrderId) {
        return this.baseMapper.getSumQty(productIds, saleOrderId);
    }

    @Override
    public List<SaleOrderProductEntity> getBySaleOrderCode(String orderNo) {
        return this.baseMapper.getBySaleOrderCode(orderNo);
    }

    @Override
    public List<String> countSign(String orderNo) {
        return this.baseMapper.countSign(orderNo);
    }

}

