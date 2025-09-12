package com.senmol.mes.workflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 记录审核员(FlowRecordUser)表实体类
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_flow_record_user")
public class FlowRecordUser extends Model<FlowRecordUser> {
    private static final long serialVersionUID = -7002738517984729687L;
    /**
     * 项目ID
     */
    @NotNull(message = "缺少父级ID", groups = ParamsValidate.Update.class)
    @TableField("pid")
    private Long pid;
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
     * 审核结果 0-未审核 1-通过 2-不通过
     */
    @TableField("result")
    private Integer result;
    /**
     * 描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 显示顺序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @OrderBy(asc = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 审核时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

}

