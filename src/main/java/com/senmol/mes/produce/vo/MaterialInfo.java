package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class MaterialInfo implements Serializable {
    private static final long serialVersionUID = -8911957956865471684L;
    /**
     * 物料ID
     */
    private Long id;
    /**
     * 二级物料父ID
     */
    private Long pid;
    /**
     * 二级物料编号
     */
    private String pCode;
    /**
     * 二级物料编号
     */
    private String pTitle;
    /**
     * 物料编号
     */
    private String code;
    /**
     * 物料
     */
    private String title;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 最小包装量
     */
    private BigDecimal minPackQty;
    /**
     * 最小起订量
     */
    private BigDecimal moq;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 库存量
     */
    private BigDecimal inventory = BigDecimal.ZERO;
    /**
     * 预占库存量
     */
    private BigDecimal preInventory = BigDecimal.ZERO;
    /**
     * 单个产品需求量
     */
    private BigDecimal singleQty;
    /**
     * 单个产品需求量 * 父级数量
     */
    private BigDecimal psingleQty;
    /**
     * 阈值量
     */
    private BigDecimal thresholdQty;
    /**
     * 采购周期
     */
    private BigDecimal purchaseCycle;
    /**
     * 级层
     */
    private Integer level;

}
