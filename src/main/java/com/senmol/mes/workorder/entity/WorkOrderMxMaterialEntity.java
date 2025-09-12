package com.senmol.mes.workorder.entity;

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
 * 工单明细物料(WorkOrderMxMaterial)表实体类
 *
 * @author makejava
 * @since 2023-02-21 10:54:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_order_mx_material")
public class WorkOrderMxMaterialEntity extends Model<WorkOrderMxMaterialEntity> {
    private static final long serialVersionUID = -2897781763526567251L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 工单明细ID
     */
    @NotNull(message = "缺少工单明细ID", groups = ParamsValidate.Insert.class)
    @TableField("mx_id")
    private Long mxId;
    /**
     * 物料ID
     */
    @NotNull(message = "缺少物料ID", groups = ParamsValidate.Insert.class)
    @TableField("material_id")
    private Long materialId;
    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String materialTitle;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    @TableField("type")
    private Integer type;
    /**
     * 单个产品使用量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 工位ID
     */
    @NotNull(message = "缺少工位ID", groups = ParamsValidate.Insert.class)
    @TableField("station_id")
    private Long stationId;
    /**
     * 工序ID
     */
    @NotNull(message = "缺少工序ID", groups = ParamsValidate.Insert.class)
    @TableField("process_id")
    private Long processId;
    /**
     * 领取数量
     */
    @TableField("receive_qty")
    private BigDecimal receiveQty;
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

