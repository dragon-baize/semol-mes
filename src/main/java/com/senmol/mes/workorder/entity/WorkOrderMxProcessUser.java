package com.senmol.mes.workorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 开报工人员(WorkOrderMxProcessUser)表实体类
 *
 * @author makejava
 * @since 2024-04-15 13:31:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_order_mx_process_user")
public class WorkOrderMxProcessUser extends Model<WorkOrderMxProcessUser> {
    private static final long serialVersionUID = 7388443291793909516L;
    /**
     * 工序ID
     */
    @TableField("mx_process_id")
    private Long mxProcessId;
    /**
     * 人员ID
     */
    @TableField("user_id")
    private Long userId;

}

