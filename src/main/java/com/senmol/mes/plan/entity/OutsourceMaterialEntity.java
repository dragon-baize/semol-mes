package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 委外-物料(OutsourceMaterial)表实体类
 *
 * @author makejava
 * @since 2023-03-09 15:59:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_outsource_material")
public class OutsourceMaterialEntity extends Model<OutsourceMaterialEntity> {
    private static final long serialVersionUID = -3971723159086043894L;
    /**
     * 委外数量
     */
    @TableField(exist = false)
    private BigDecimal osQty;
    /**
     * 委外计划ID
     */
    @TableField("outsource_id")
    private Long outsourceId;
    /**
     * 物料ID
     */
    @TableField("material_id")
    private Long materialId;
    /**
     * 物料编号
     */
    @TableField(exist = false)
    private String materialCode;
    /**
     * 物料
     */
    @TableField(exist = false)
    private String materialTitle;
    /**
     * 单个需求量
     */
    @TableField("single_qty")
    private BigDecimal singleQty;
    /**
     * 总需求数量
     */
    @TableField("qty")
    private BigDecimal qty;
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
    @JsonIgnore
    @Version
    @TableField("version")
    private Integer version;

}

