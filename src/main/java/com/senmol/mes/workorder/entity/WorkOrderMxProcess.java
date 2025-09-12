package com.senmol.mes.workorder.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 工单工序(WorkOrderMxProcess)表实体类
 *
 * @author makejava
 * @since 2023-11-23 14:29:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_order_mx_process")
public class WorkOrderMxProcess extends Model<WorkOrderMxProcess> {
    private static final long serialVersionUID = 1992909177734227443L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 明细ID
     */
    @NotNull(message = "缺少工单", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("mx_id")
    private Long mxId;
    /**
     * 工位ID
     */
    @TableField("station_id")
    private Long stationId;
    /**
     * 设备
     */
    @TableField("device_id")
    private Long deviceId;
    /**
     * 工序ID
     */
    @NotNull(message = "缺少工序", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("process_id")
    private Long processId;
    /**
     * 工序序号
     */
    @TableField("serial_no")
    private Integer serialNo;
    /**
     * 良品数量
     */
    @NotNull(message = "请输入良品数量", groups = ParamsValidate.Update.class)
    @TableField("non_defective")
    private BigDecimal nonDefective;
    /**
     * 不良品数量
     */
    @TableField("defective")
    private BigDecimal defective;
    /**
     * 返工数量
     */
    @TableField("rework")
    private BigDecimal rework;
    /**
     * 开工时间
     */
    @TableField("begin_time")
    private LocalDateTime beginTime;
    /**
     * 报工时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;
    /**
     * 良率
     */
    @TableField("yield")
    private BigDecimal yield;
    /**
     * 状态 0-为报工 1-已报工
     */
    @TableField("status")
    private Integer status;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 开报工人员
     */
    @NotEmpty(message = "缺少开/报工人员")
    @TableField(exist = false)
    private Set<Long> userIds;
    /**
     * 不良模式数据
     */
    @TableField(exist = false)
    private List<WorkOrderMxBadMode> badModes;

}

