package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-05-16 11:36:23
 */
@Data
public class PurchaseReturnsTotal implements Serializable {
    private static final long serialVersionUID = 6909692971346854182L;
    /**
     * 数量合计
     */
    private BigDecimal totalQty;
    /**
     * 单价
     */
    private BigDecimal totalPrice;
    /**
     * 税率
     */
    private BigDecimal totalTaxRate;
    /**
     * 含税价
     */
    private BigDecimal totalTaxPrice;

}
