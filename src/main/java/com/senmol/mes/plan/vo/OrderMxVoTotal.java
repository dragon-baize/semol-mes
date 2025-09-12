package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-05-16 09:21:16
 */
@Data
public class OrderMxVoTotal implements Serializable {
    private static final long serialVersionUID = -2621023517831796353L;
    /**
     * 发货数量合计
     */
    private BigDecimal totalRealityQty;
    /**
     * 发货金额合计
     */
    private BigDecimal totalRealityPrice;
    /**
     * 发货含税价合计
     */
    private BigDecimal totalRealityTaxPrice;
    /**
     * 订货数量合计
     */
    private BigDecimal totalQty;
    /**
     * 订货金额合计
     */
    private BigDecimal totalPrice;
    /**
     * 订货含税价合计
     */
    private BigDecimal totalTaxPrice;
    /**
     * 未发货数量合计
     */
    private BigDecimal totalUnshippedQty;
    /**
     * 未发货金额合计
     */
    private BigDecimal totalUnshippedPrice;
    /**
     * 未发货含税价合计
     */
    private BigDecimal totalUnshippedTaxPrice;

}
