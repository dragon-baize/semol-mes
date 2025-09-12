package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class PurchaseOrderVo implements Serializable {
    private static final long serialVersionUID = -7492425158354407700L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 单号
     */
    private String orderNo;
    /**
     * 名称
     */
    private String title;
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
     * 物品类型 1-半成品 2-物料
     */
    private Integer type;
    /**
     * 请购单ID
     */
    private Long requisitionId;
    /**
     * 请购单编号
     */
    private String requisitionCode;
    /**
     * 请购单
     */
    private String requisitionTitle;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 确认数量
     */
    private BigDecimal confirmQty;
    /**
     * 入库数量
     */
    private BigDecimal storageQty;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 需求时间
     */
    private LocalDate requireTime;
    /**
     * 采购周期
     */
    private BigDecimal purchaseCycle;
    /**
     * 供应商
     */
    private Long supplierId;
    /**
     * 供应商
     */
    private String supplierTitle;
    /**
     * 建议采购时间
     */
    private LocalDate advicePurchaseTime;
    /**
     * 是否客供货 0-否 1-是
     */
    private Integer isCustomerSupply;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    private BigDecimal taxPrice;
    /**
     * 状态 0-待定 1-已确认 2-已完成
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 创建人
     */
    private String createUserName;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;

}
