package com.senmol.mes.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 添加项文件(NapeFile)表实体类
 *
 * @author makejava
 * @since 2023-03-21 09:43:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_nape_file")
public class NapeFile extends Model<NapeFile> {
    private static final long serialVersionUID = 7114360900044411871L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 添加项ID
     */
    @NotNull(message = "阶段项ID不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("nape_id")
    private Long napeId;
    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("manage_id")
    private Long manageId;
    /**
     * 文件原名
     */
    @NotBlank(message = "文件原名不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("original_title")
    private String originalTitle;
    /**
     * 文件实际名
     */
    @TableField("reality_title")
    private String realityTitle;
    /**
     * 文件上传路径
     */
    @TableField("file_path")
    private String filePath;
    /**
     * 类型 0-文件 1-数据
     */
    @NotNull(message = "请选择文件类型", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("type")
    private Integer type;
    /**
     * 是否审批 0-否 1-是
     */
    @NotNull(message = "请选择是否审批", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("is_approve")
    private Integer isApprove;
    /**
     * 审核结果 0-待审 1-通过 2-不通过
     */
    @TableField("approve_result")
    private Integer approveResult;
    /**
     * 审批超时时间
     */
    @TableField("time_out")
    private LocalDateTime timeOut;
    /**
     * 自定义版本号
     */
    @TableField("revision")
    private String revision;
    /**
     * 文件说明
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @OrderBy
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
     * 审核员ID列表
     */
    @TableField(exist = false)
    private List<Long> userIds;

}

