package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class ProductQty implements Serializable {
    private static final long serialVersionUID = 1936221102464292186L;
    /**
     * 产品ID
     */
    private Long goodsId;
    /**
     * 数量
     */
    private BigDecimal qty;
}
