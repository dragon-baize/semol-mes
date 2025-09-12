package com.senmol.mes.produce.entity;

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
import java.time.LocalDateTime;

/**
 * 产品管理(Product)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_product")
public class ProductEntity extends Model<ProductEntity> {
    private static final long serialVersionUID = -7950249694645764199L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @NotBlank(message = "产品编号不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "code", condition = SqlCondition.LIKE)
    private String code;
    /**
     * 名称
     */
    @NotBlank(message = "产品名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 产线ID
     */
    @TableField("product_line_id")
    private Long productLineId;
    /**
     * 产线
     */
    @TableField(exist = false)
    private String productLineCode;
    /**
     * 产线
     */
    @TableField(exist = false)
    private String productLineTitle;
    /**
     * 规格
     */
    @TableField("specs")
    private String specs;
    /**
     * 生产模式 0-自制 1-委外 2-外购
     */
    @NotNull(message = "请选择生产模式", groups = ParamsValidate.Insert.class)
    @TableField("product_mode")
    private Integer productMode;
    /**
     * 生产周期
     */
    @TableField("product_cycle")
    private BigDecimal productCycle;
    /**
     * 外购周期
     */
    @TableField("purchase_cycle")
    private BigDecimal purchaseCycle;
    /**
     * 委外周期
     */
    @TableField("outsource_cycle")
    private BigDecimal outsourceCycle;
    /**
     * 每批次产品数量
     */
    @TableField("per_batch_qty")
    private BigDecimal perBatchQty;
    /**
     * 类型 0-半成品 1-成品
     */
    @NotNull(message = "请选择产品类型", groups = ParamsValidate.Insert.class)
    @TableField("type")
    private Integer type;
    /**
     * 单位ID
     */
    @TableField("unit_id")
    private Long unitId;
    /**
     * 单位
     */
    @TableField(exist = false)
    private String unitTitle;
    /**
     * 产品良率/%
     */
    @TableField("yield")
    private BigDecimal yield;
    /**
     * 寿命信息/天
     */
    @TableField("life_info")
    private Integer lifeInfo;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 状态ID
     */
    @TableField("status")
    private Long status;
    /**
     * 状态
     */
    @TableField(exist = false)
    private String statusTitle;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @OrderBy
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

