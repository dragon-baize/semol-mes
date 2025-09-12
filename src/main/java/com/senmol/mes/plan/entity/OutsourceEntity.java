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
 * 委外计划(Outsource)表实体类
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_outsource")
public class OutsourceEntity extends Model<OutsourceEntity> {
    private static final long serialVersionUID = -1197362908134860493L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "计划名称不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("title")
    private String title;
    /**
     * 委外编号
     */
    @TableField("code")
    private String code;
    /**
     * 产线ID
     */
    @TableField(exist = false)
    private Long productLineId;
    /**
     * 销售订单ID
     */
    @TableField("order_no")
    private Long orderNo;
    /**
     * 销售订单编号
     */
    @TableField(exist = false)
    private String orderCode;
    /**
     * 供应商ID
     */
    @TableField("supplier_id")
    private Long supplierId;
    /**
     * 供应商
     */
    @TableField(exist = false)
    private String supplierTitle;
    /**
     * 产品ID
     */
    @NotNull(message = "请选择产品", groups = ParamsValidate.Insert.class)
    @TableField("product_id")
    private Long productId;
    /**
     * 产品编号
     */
    @TableField(exist = false)
    private String productCode;
    /**
     * 产品
     */
    @TableField(exist = false)
    private String productTitle;
    /**
     * 良率
     */
    @TableField(exist = false)
    private BigDecimal yield;
    /**
     * 建议委外时间
     */
    @TableField("expect_date")
    private LocalDate expectDate;
    /**
     * 交货日期
     */
    @TableField("delivery_date")
    private LocalDate deliveryDate;
    /**
     * 周期
     */
    @TableField("cycle")
    private BigDecimal cycle;
    /**
     * 数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 已入库数量
     */
    @TableField("storage_qty")
    private BigDecimal storageQty;
    /**
     * 是否已创建 0-否 1-是
     */
    @TableField("is_create")
    private Integer isCreate;
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
     * 状态 0-待定 1-发货 2-收货 3-完成
     */
    @TableField("status")
    private Integer status;
    /**
     * 是否MRP导入 0-是 1-否
     */
    @TableField("mrp")
    private Integer mrp;
    /**
     * mrp-id
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

