package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class MaterialPojo implements Serializable {
    private static final long serialVersionUID = 8309407865600504333L;
    /**
     * 销售单编号
     */
    private String saleOrderCode;
    /**
     * 计划编号
     */
    private String planCode;
    /**
     * 任务单ID
     */
    private Long pid;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品编号
     */
    private String productCode;
    /**
     * 产品名称
     */
    private String productTitle;
    /**
     * 物料ID
     */
    private Long materialId;
    /**
     * 物料编号
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialTitle;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 物料类型
     */
    private Integer type;
    /**
     * 领取数量
     */
    private BigDecimal receiveQty;
    /**
     * 上料数量
     */
    private BigDecimal feedQty;

}
