package com.senmol.mes.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 添加项(StageNape)表实体类
 *
 * @author makejava
 * @since 2023-03-21 09:27:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_stage_nape")
public class StageNape extends Model<StageNape> {
    private static final long serialVersionUID = -7220579573209364360L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 添加项名称
     */
    @NotBlank(message = "添加项名称不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 责任部门
     */
    @NotNull(message = "请选择责任部门", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("dept_id")
    private Long deptId;
    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptTitle;
    /**
     * 选项描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 输入类型 0-文件 1-数据
     */
    @NotNull(message = "请选择输入类型", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("type")
    private Integer type;
    /**
     * 是否审核 0-不审核 1-审核
     */
    @NotNull(message = "请选择是否审核", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("is_approve")
    private Integer isApprove;
    /**
     * 所属阶段 0-初始 1-概念 2-设计 3-工艺 4-量产
     */
    @NotNull(message = "所属阶段不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("belong")
    private Integer belong;
    /**
     * 是否查阅 0-未阅读 1-已阅读
     */
    @TableField("is_view")
    private Integer isView;
    /**
     * 审核结果 0-待审 1-通过 2-不通过
     */
    @TableField("approve_result")
    private Integer approveResult;
    /**
     * 状态 0-没有任何文件 1-存在文件未超时未审 2-存在文件超时未审 3-正常提交
     */
    @TableField("status")
    private Integer status;
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
    @JsonIgnore
    @Version
    @TableField("version")
    private Integer version;
    /**
     * 审核人ID列表
     */
    @TableField(exist = false)
    private List<Long> userIds;

}

