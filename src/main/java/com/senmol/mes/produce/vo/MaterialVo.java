package com.senmol.mes.produce.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class MaterialVo implements Serializable {
    private static final long serialVersionUID = 7299926011795327126L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 编号
     */
    @ExcelProperty(value = "存货编号")
    private String code;
    /**
     * 名称
     */
    @ExcelProperty(value = "存货全名")
    private String title;
    /**
     * 仓库类型 0-原料 1-成品 2-半成品
     */
    private Integer warehouseType;
    /**
     * 采购周期
     */
    private BigDecimal purchaseCycle;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    @ExcelProperty(value = "基本单位")
    private String unitTitle;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 最小包装量
     */
    private BigDecimal minPackQty;
    /**
     * 最小起订量
     */
    private BigDecimal moq;
    /**
     * 类型 0-非原料 1-原料
     */
    private Integer type;
    /**
     * 型号
     */
    private String model;
    /**
     * 预计价格
     */
    private BigDecimal valuation;
    /**
     * 寿命类型 0-手动维护 1-自动维护
     */
    private Integer lifeType;
    /**
     * 寿命信息
     */
    private Integer lifeInfo;
    /**
     * 提前提醒天数
     */
    private Integer reminderTime;
    /**
     * 规格描述
     */
    @ExcelProperty(value = "存货规格")
    private String remarks;

}
