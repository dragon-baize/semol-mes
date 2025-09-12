package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-05-16 16:06:29
 */
@Data
public class RestockTotal implements Serializable {
    private static final long serialVersionUID = -3301749439931734859L;
    /**
     * 数量合计
     */
    private BigDecimal totalQty;
    /**
     * 含税单价合计
     */
    private BigDecimal totalTaxPrice;
    /**
     * 价税合计合计
     */
    private BigDecimal totalPriceTax;

}
