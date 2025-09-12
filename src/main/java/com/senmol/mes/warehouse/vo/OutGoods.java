package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class OutGoods implements Serializable {
    private static final long serialVersionUID = -7458803222073180834L;
    /**
     * 入库批次
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
     * 类型
     */
    private Integer type;
    /**
     * 唯一码
     */
    private String qrCode;
    /**
     * 出库数量
     */
    private BigDecimal qty;
    /**
     * 使用数量
     */
    private BigDecimal usedQty;
    /**
     * 单位
     */
    private String unitTitle;

}
