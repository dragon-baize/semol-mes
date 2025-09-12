package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-05-16 09:54:03
 */
@Data
public class DeliveryVoTotal implements Serializable {
    private static final long serialVersionUID = -6268908925351069637L;
    /**
     * 数量合计
     */
    private BigDecimal totalQty;
    /**
     * 单价合计
     */
    private BigDecimal totalPrice;
    /**
     * 金额合计
     */
    private BigDecimal totalAmount;
    /**
     * 含税单价合计
     */
    private BigDecimal totalTaxPrice;
    /**
     * 价税合计合计
     */
    private BigDecimal totalPriceTax;

}
