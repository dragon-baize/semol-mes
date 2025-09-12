package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-08-05 15:10:31
 */
@Data
public class SaleInvoiceTotal  implements Serializable {
    private static final long serialVersionUID = 7639911437338334394L;
    /**
     * 总数量
     */
    private BigDecimal qty;
    /**
     * 总价税合计
     */
    private BigDecimal total;

}
