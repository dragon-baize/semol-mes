package com.senmol.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 清单-物料(BomMaterial)表实体类
 *
 * @author makejava
 * @since 2023-01-31 15:46:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_bom_material")
public class BomMaterialEntity extends Model<BomMaterialEntity> {
    private static final long serialVersionUID = 3579502617356177743L;
    /**
     * 清单ID
     */
    @NotNull(message = "缺少清单ID", groups = ParamsValidate.Insert.class)
    @TableField("bom_id")
    private Long bomId;
    /**
     * 物料ID
     */
    @NotNull(message = "请选择物料", groups = ParamsValidate.Insert.class)
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
     * 单位
     */
    @TableField(exist = false)
    private String unitTitle;
    /**
     * 单个产品需求量
     */
    @NotNull(message = "请输入单个产品需求量", groups = ParamsValidate.Insert.class)
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 工序ID
     */
    @TableField("process_id")
    private Long processId;
    /**
     * 工序
     */
    @TableField(exist = false)
    private String processTitle;
    /**
     * 工序序号
     */
    @TableField("serial_no")
    private Integer serialNo;
    /**
     * 类型 0-原料 1-半成品
     */
    @TableField("type")
    private Integer type;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    @TableField("create_user")
    private Long createUser;

}

