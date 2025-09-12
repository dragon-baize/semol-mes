package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购单(PurchaseOrder)表实体类
 *
 * @author makejava
 * @since 2023-03-13 09:25:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_purchase_order")
public class PurchaseOrderEntity extends Model<PurchaseOrderEntity> {
    private static final long serialVersionUID = 5317984694280104731L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 单号
     */
    @TableField(value = "order_no", updateStrategy = FieldStrategy.NEVER)
    private String orderNo;
    /**
     * 名称
     */
    @NotBlank(message = "采购单名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField("title")
    private String title;
    /**
     * 物料ID
     */
    @NotNull(message = "请选择物料", groups = ParamsValidate.Insert.class)
    @TableField("material_id")
    private Long materialId;
    /**
     * 物料ID
     */
    @TableField(exist = false)
    private String materialCode;
    /**
     * 物品类型 1-半成品 2-物料
     */
    @TableField("type")
    private Integer type;
    /**
     * 请购单ID
     */
    @TableField("requisition_id")
    private Long requisitionId;
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空", groups = ParamsValidate.Insert.class)
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 确认数量
     */
    @NotNull(message = "确认数量不能为空", groups = ParamsValidate.Insert.class)
    @TableField("confirm_qty")
    private BigDecimal confirmQty;
    /**
     * 入库数量
     */
    @TableField("storage_qty")
    private BigDecimal storageQty;
    /**
     * 需求时间
     */
    @TableField("require_time")
    private LocalDate requireTime;
    /**
     * 采购周期
     */
    @TableField("purchase_cycle")
    private BigDecimal purchaseCycle;
    /**
     * 供应商
     */
    @TableField("supplier_id")
    private Long supplierId;
    /**
     * 建议采购时间
     */
    @TableField("advice_purchase_time")
    private LocalDate advicePurchaseTime;
    /**
     * 是否客供货 0-否 1-是
     */
    @TableField("is_customer_supply")
    private Integer isCustomerSupply;
    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;
    /**
     * 税率
     */
    @TableField("tax_rate")
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    @TableField("tax_price")
    private BigDecimal taxPrice;
    /**
     * 状态 0-待定 1-已确认 2-已完成 3-部分入库
     */
    @TableField("status")
    private Integer status;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

}

