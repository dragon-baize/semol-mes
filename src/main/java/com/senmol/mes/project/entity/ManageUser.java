package com.senmol.mes.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 项目审核(ManageUser)表实体类
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_manage_user")
public class ManageUser extends Model<ManageUser> {
    private static final long serialVersionUID = 6562125400634290895L;
    /**
     * 项目ID
     */
    @NotNull(message = "缺少项目ID", groups = ParamsValidate.Update.class)
    @TableField("manage_id")
    private Long manageId;
    /**
     * 审核员ID
     */
    @NotNull(message = "缺少审核员ID", groups = ParamsValidate.Update.class)
    @TableField("user_id")
    private Long userId;
    /**
     * 审核员
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 是否已审核 0-未审核 1-通过 2-不通过
     */
    @NotNull(message = "请选择审核结果", groups = ParamsValidate.Update.class)
    @TableField("is_approve")
    private Integer isApprove;
    /**
     * 描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 版本号
     */
    @JsonIgnore
    @Version
    @TableField("version")
    private Integer version;

}

