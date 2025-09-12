package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class StorageInfo implements Serializable {
    private static final long serialVersionUID = -3368509513226589016L;
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
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 生产模式
     */
    private Integer productMode;
    /**
     * 最小包装量
     */
    private BigDecimal minPackQty;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer type;
    /**
     * 剩余量
     */
    private BigDecimal residueQty;
    /**
     * 寿命类型 0-手动维护 1-自动维护
     */
    private Integer lifeType;
}
