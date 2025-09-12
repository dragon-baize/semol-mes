package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-05-15 17:03:18
 */
@Data
public class PurchaseOrderTotal implements Serializable {
    private static final long serialVersionUID = -4240546519059636687L;
    /**
     * 建议采购数量合计
     */
    private BigDecimal totalQty;
    /**
     * 实际采购数量合计
     */
    private BigDecimal totalConfirmQty;
    /**
     * 已入库数量合计
     */
    private BigDecimal totalStorageQty;
    /**
     * 单价合计
     */
    private BigDecimal totalPrice;
    /**
     * 税额合计
     */
    private BigDecimal totalTaxRate;
    /**
     * 价税合计合计
     */
    private BigDecimal totalTaxPrice;

}
