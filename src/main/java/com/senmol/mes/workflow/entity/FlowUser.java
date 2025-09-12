package com.senmol.mes.workflow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 审批人员(FlowUser)表实体类
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_flow_user")
public class FlowUser extends Model<FlowUser> {
    private static final long serialVersionUID = 6485111550380124417L;
    /**
     * 流程ID
     */
    @TableField("flow_id")
    private Long flowId;
    /**
     * 审核员ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 审核员
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 审核顺序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

}

