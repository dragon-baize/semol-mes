package com.senmol.mes.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.SaleOrderProductEntity;
import com.senmol.mes.plan.vo.ProductQty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 销售订单发货(SaleOrderProduct)表服务接口
 *
 * @author makejava
 * @since 2023-07-18 09:15:46
 */
public interface SaleOrderProductService extends IService<SaleOrderProductEntity> {

    /**
     * 批量修改
     *
     * @param products 产品信息
     */
    void modifyBatchById(List<SaleOrderProductEntity> products, String saleOrderCode);

    /**
     * 批量修改
     *
     * @param map 产品信息
     */
    void modifyBatchById2(Map<Long, BigDecimal> map, String saleOrderCode);

    /**
     * 获取产品销售订单总量
     *
     * @param productIds  产品ID列表
     * @param saleOrderId 销售单ID
     * @return 总量
     */
    List<ProductQty> getSumQty(List<Long> productIds, Long saleOrderId);

    /**
     * 销售单号获取销售单明细
     *
     * @param orderNo 销售单号
     * @return 结果
     */
    List<SaleOrderProductEntity> getBySaleOrderCode(String orderNo);

    /**
     * 计算是否发货完成
     *
     * @param orderNo 销售单号
     * @return 结果
     */
    List<String> countSign(String orderNo);

}

