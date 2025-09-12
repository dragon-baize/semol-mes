package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class GoodsMx implements Serializable {
    private static final long serialVersionUID = 914319739739663738L;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 数量
     */
    private BigDecimal residueQty;

}
