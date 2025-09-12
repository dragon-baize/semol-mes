package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-05-16 09:02:01
 */
@Data
public class PurchaseInvoiceTotal implements Serializable {
    private static final long serialVersionUID = 5626908962676729109L;
    /**
     * 入库总数量合计
     */
    private BigDecimal totalStorageQty;
    /**
     * 价税合计合计
     */
    private BigDecimal totalTaxPrice;

}
