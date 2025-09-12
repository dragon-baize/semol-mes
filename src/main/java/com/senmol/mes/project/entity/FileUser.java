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
 * 文件审核(FileUser)表实体类
 *
 * @author makejava
 * @since 2023-03-21 13:37:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_file_user")
public class FileUser extends Model<FileUser> {
    private static final long serialVersionUID = 1903046605168245220L;
    /**
     * 文件ID
     */
    @NotNull(message = "缺少文件ID", groups = ParamsValidate.Update.class)
    @TableField("file_id")
    private Long fileId;
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

