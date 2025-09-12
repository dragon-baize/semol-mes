package com.senmol.mes.workorder.page;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-08-13 10:25:24
 */
@Data
public class RetrospectTotal implements Serializable {
    private static final long serialVersionUID = -5714938049761295777L;
    /**
     * 数量合计
     */
    private BigDecimal totalQty;
    /**
     * 含税单价合计
     */
    private BigDecimal totalTaxPrice;
    /**
     * 总价合计
     */
    private BigDecimal totalPrice;

}
