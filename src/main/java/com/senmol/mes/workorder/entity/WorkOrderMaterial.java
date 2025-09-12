package com.senmol.mes.workorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 任务单物料(WorkOrderMaterial)表实体类
 *
 * @author makejava
 * @since 2023-11-07 13:39:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_order_material")
public class WorkOrderMaterial extends Model<WorkOrderMaterial> {
    private static final long serialVersionUID = -3279605698447296416L;
    /**
     * 工单ID
     */
    @TableField("mx_id")
    private Long mxId;
    /**
     * 物料ID
     */
    @TableField("material_id")
    private Long materialId;
    /**
     * 单个产品需求量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 工位ID
     */
    @TableField("station_id")
    private Long stationId;
    /**
     * 设备ID
     */
    @TableField("device_id")
    private Long deviceId;
    /**
     * 工序ID
     */
    @TableField("process_id")
    private Long processId;
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

}

