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
 * 物料管理(Material)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_material")
public class MaterialEntity extends Model<MaterialEntity> {
    private static final long serialVersionUID = -8640783322565434662L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @NotBlank(message = "物料编号不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "code", condition = SqlCondition.LIKE)
    private String code;
    /**
     * 名称
     */
    @NotBlank(message = "物料名称不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 仓库类型 0-原料 1-成品 2-半成品
     */
    @TableField("warehouse_type")
    private Integer warehouseType;
    /**
     * 采购周期
     */
    @TableField("purchase_cycle")
    private BigDecimal purchaseCycle;
    /**
     * 单位ID
     */
    @NotNull(message = "请选择单位", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("unit_id")
    private Long unitId;
    /**
     * 单位
     */
    @TableField(exist = false)
    private String unitTitle;
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 最小包装量
     */
    @TableField("min_pack_qty")
    private BigDecimal minPackQty;
    /**
     * 最小起订量
     */
    @TableField("moq")
    private BigDecimal moq;
    /**
     * 类型 0-非原料 1-原料
     */
    @NotNull(message = "请选择物料类型", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("type")
    private Integer type;
    /**
     * 型号
     */
    @TableField("model")
    private String model;
    /**
     * 预计价格
     */
    @TableField("valuation")
    private BigDecimal valuation;
    /**
     * 寿命类型 0-手动维护 1-自动维护
     */
    @NotNull(message = "请选择寿命类型", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("life_type")
    private Integer lifeType;
    /**
     * 寿命信息
     */
    @TableField("life_info")
    private Integer lifeInfo;
    /**
     * 提前提醒天数
     */
    @TableField("reminder_time")
    private Integer reminderTime;
    /**
     * 规格描述
     */
    @TableField("remarks")
    private String remarks;
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

