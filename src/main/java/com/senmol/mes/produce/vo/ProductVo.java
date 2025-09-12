package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class ProductVo implements Serializable {
    private static final long serialVersionUID = -7413895010552955864L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String title;
    /**
     * 产线ID
     */
    private Long productLineId;
    /**
     * 规格
     */
    private String specs;
    /**
     * 生产模式 0-自制 1-委外 2-外购
     */
    private Integer productMode;
    /**
     * 生产周期
     */
    private BigDecimal productCycle;
    /**
     * 外购周期
     */
    private BigDecimal purchaseCycle;
    /**
     * 委外周期
     */
    private BigDecimal outsourceCycle;
    /**
     * 每批次产品数量
     */
    private BigDecimal perBatchQty;
    /**
     * 类型 0-半成品 1-成品
     */
    private Integer type;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 产品良率/%
     */
    private BigDecimal yield;
    /**
     * 寿命信息/天
     */
    private Integer lifeInfo;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 状态ID
     */
    private Long status;
}
