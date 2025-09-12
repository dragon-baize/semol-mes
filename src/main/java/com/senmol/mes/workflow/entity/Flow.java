package com.senmol.mes.workflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 审批流程(Flow)表实体类
 *
 * @author makejava
 * @since 2023-03-24 14:25:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_flow")
public class Flow extends Model<Flow> {
    private static final long serialVersionUID = -9086418373644859312L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 菜单
     */
    @NotNull(message = "请选择流程模块", groups = ParamsValidate.Insert.class)
    @TableField("title")
    private String title;
    /**
     * 状态 0-禁用 1-启用
     */
    @NotNull(message = "请选择流程状态", groups = ParamsValidate.Insert.class)
    @TableField("status")
    private Integer status;
    /**
     * 是否开始流转 0-否 1-是
     */
    @TableField("is_use")
    private Integer isUse;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
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
     * 创建人
     */
    @TableField(exist = false)
    private String createUserName;
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
    /**
     * 审核员ID-审批顺序列表
     */
    @NotEmpty(message = "请添加流程审核员")
    @TableField(exist = false)
    private List<FlowUser> flowUsers;

}

