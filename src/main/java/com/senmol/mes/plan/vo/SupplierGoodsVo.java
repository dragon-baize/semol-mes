package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class SupplierGoodsVo implements Serializable {
    private static final long serialVersionUID = -2334824025579643071L;
    /**
     * 供应商ID
     */
    private Long pid;
    /**
     * 物料ID
     */
    private Long materialId;
    /**
     * 客户物料编号
     */
    private String cusMatCode;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    private BigDecimal taxPrice;

}
