package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.SaleOrderProductEntity;
import com.senmol.mes.plan.vo.ProductQty;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 销售订单发货(SaleOrderProduct)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-18 09:15:46
 */
public interface SaleOrderProductMapper extends BaseMapper<SaleOrderProductEntity> {

    /**
     * 批量修改
     *
     * @param entities 产品信息
     */
    void modifyBatchById(@Param("entities") List<SaleOrderProductEntity> entities,
                         @Param("saleOrderCode") String saleOrderCode);

    /**
     * 批量修改
     *
     * @param map 产品信息
     */
    void modifyBatchById2(@Param("map") Map<Long, BigDecimal> map, @Param("saleOrderCode") String saleOrderCode);

    /**
     * 获取产品销售订单总量
     *
     * @param productIds  产品ID列表
     * @param saleOrderId 销售单ID
     * @return 总量
     */
    List<ProductQty> getSumQty(@Param("productIds") List<Long> productIds, @Param("saleOrderId") Long saleOrderId);

    /**
     * 销售单号获取销售单明细
     *
     * @param orderNo 销售单号
     * @return 结果
     */
    List<SaleOrderProductEntity> getBySaleOrderCode(@Param("orderNo") String orderNo);

    /**
     * 计算是否发货完成
     *
     * @param orderNo 销售单号
     * @return 结果
     */
    List<String> countSign(@Param("orderNo") String orderNo);

}
