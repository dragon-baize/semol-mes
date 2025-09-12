package com.senmol.mes.workorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * (WorkOrderMxBadMode)表实体类
 *
 * @author makejava
 * @since 2023-12-26 09:30:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_order_mx_bad_mode")
public class WorkOrderMxBadMode extends Model<WorkOrderMxBadMode> {
    private static final long serialVersionUID = -857520881300125044L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 工单ID
     */
    @TableField("mx_id")
    private Long mxId;
    /**
     * 工序ID
     */
    @TableField("process_id")
    private Long processId;
    /**
     * 不良模式ID
     */
    @TableField("bad_mode_id")
    private Long badModeId;
    /**
     * 不良模式名称
     */
    @TableField(exist = false)
    private String badModeTitle;
    /**
     * 不良模式数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

}

