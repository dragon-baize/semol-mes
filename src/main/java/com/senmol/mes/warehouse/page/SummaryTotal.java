package com.senmol.mes.warehouse.page;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-08-08 15:37:50
 */
@Data
public class SummaryTotal implements Serializable {
    private static final long serialVersionUID = 5762509837289857699L;
    /**
     * 期初结存合计
     */
    private BigDecimal totalPreQty;
    /**
     * 期初结存金额合计
     */
    private BigDecimal totalPreAmount;
    /**
     * 本期入合计
     */
    private BigDecimal totalInQty;
    /**
     * 本期入金额合计
     */
    private BigDecimal totalInAmount;
    /**
     * 本期出合计
     */
    private BigDecimal totalOutQty;
    /**
     * 本期出金额合计
     */
    private BigDecimal totalOutAmount;
    /**
     * 期末结存合计
     */
    private BigDecimal totalLastQty;
    /**
     * 期末结存金额合计
     */
    private BigDecimal totalLastAmount;

}
