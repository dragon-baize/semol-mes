package com.senmol.mes.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class OutboundMaterial implements Serializable {
    private static final long serialVersionUID = -1324271608047729100L;
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
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer type;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 库存数量
     */
    private BigDecimal storageQty;
    /**
     * 基础数量
     */
    private BigDecimal baseQty;
    /**
     * 单个产品需求量
     */
    private BigDecimal qty;

}
