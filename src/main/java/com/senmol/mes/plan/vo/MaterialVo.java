package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class MaterialVo implements Serializable {
    private static final long serialVersionUID = -3950479856530663490L;
    /**
     * 物料ID
     */
    private Long materialId;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 类型 0-原料 1-半成品
     */
    private Integer type;
    /**
     * 半成品销售未完成订单量
     */
    private BigDecimal hpQty;
}
