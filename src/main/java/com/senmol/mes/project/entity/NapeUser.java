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
 * 添加项审核(NapeUser)表实体类
 *
 * @author makejava
 * @since 2023-03-21 17:07:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_nape_user")
public class NapeUser extends Model<NapeUser> {
    private static final long serialVersionUID = -9222268764820304203L;
    /**
     * 添加项ID
     */
    @NotNull(message = "阶段项ID不能为空", groups = ParamsValidate.Update.class)
    @TableField("nape_id")
    private Long napeId;
    /**
     * 审核人ID
     */
    @NotNull(message = "审核人ID不能为空", groups = ParamsValidate.Update.class)
    @TableField("user_id")
    private Long userId;
    /**
     * 审核人
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 是否已审核 0-待审 1-通过 2-不通过
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

