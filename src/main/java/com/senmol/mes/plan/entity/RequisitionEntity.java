package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 请购单(Requisition)表实体类
 *
 * @author makejava
 * @since 2023-03-13 15:57:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_requisition")
public class RequisitionEntity extends Model<RequisitionEntity> {
    private static final long serialVersionUID = -4669485167144902497L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @TableField("code")
    private String code;
    /**
     * 名称
     */
    @TableField("title")
    private String title;
    /**
     * 物料ID
     */
    @TableField("material_id")
    private Long materialId;
    /**
     * 物料
     */
    @TableField(exist = false)
    private String materialCode;
    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;
    /**
     * 销售订单ID
     */
    @TableField("sale_order_id")
    private Long saleOrderId;
    /**
     * 数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 需求时间
     */
    @TableField("demand_time")
    private LocalDate demandTime;
    /**
     * 采购周期
     */
    @TableField("purchase_cycle")
    private BigDecimal purchaseCycle;
    /**
     * 单位ID
     */
    @TableField("unit_id")
    private Long unitId;
    /**
     * 建议采购日期
     */
    @TableField("advice_purchase_date")
    private LocalDate advicePurchaseDate;
    /**
     * 建议采购数量
     */
    @TableField("advice_qty")
    private BigDecimal adviceQty;
    /**
     * 状态 0-未生成 1-已生成
     */
    @TableField("status")
    private Integer status;
    /**
     * 是否MRP导入 0-是 1-否
     */
    @TableField("mrp")
    private Integer mrp;
    /**
     * mrp_id
     */
    @TableField(exist = false)
    private Long mrpId;
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
     * 创建人
     */
    @TableField(exist = false)
    private String createUserName;
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

