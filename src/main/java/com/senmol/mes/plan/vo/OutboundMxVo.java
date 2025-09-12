package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class OutboundMxVo implements Serializable {
    private static final long serialVersionUID = 8783666360645589225L;
    /**
     * 入库批次号
     */
    private String batchNo;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 物品编号
     */
    private String goodsCode;
    /**
     * 物品
     */
    private String goodsTitle;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer type;
    /**
     * 出库数量
     */
    private BigDecimal qty;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    private BigDecimal taxPrice;
    /**
     * 含税金额
     */
    private BigDecimal taxAmount;

}
