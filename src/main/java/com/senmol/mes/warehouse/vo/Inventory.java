package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dragon-xiaobai
 * @since 2025-03-26 17:22:51
 */
@Data
public class Inventory implements Serializable {
    private static final long serialVersionUID = 8623553035975219873L;
    /**
     * 出入库类型 1-入库 2-出库
     */
    private Integer sign;
    /**
     * 出入库表ID
     */
    private Long id;
    /**
     * 单号
     */
    private String code;
    /**
     * 检测号
     */
    private String siCode;
    /**
     * 批次号
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
     * 物品名称
     */
    private String goodsTitle;
    /**
     * 物料类型 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer type;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 摘要
     */
    private String remarks;
    /**
     * 出入库时间
     */
    private LocalDateTime createTime;

}
