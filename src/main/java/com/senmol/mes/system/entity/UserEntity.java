package com.senmol.mes.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户(User)表实体类
 *
 * @author makejava
 * @since 2022-11-22 13:25:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class UserEntity extends Model<UserEntity> {
    private static final long serialVersionUID = 6439725873590000643L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "username", updateStrategy = FieldStrategy.IGNORED)
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "password", updateStrategy = FieldStrategy.IGNORED)
    private String password;
    /**
     * 账号标识 0-MES 1-项目管理 2-通用
     */
    @TableField("type")
    private Integer type;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "real_name", condition = SqlCondition.LIKE)
    private String realName;
    /**
     * 性别 1男 2女
     */
    @TableField("gender")
    private Integer gender;
    /**
     * 手机号码
     */
    @TableField("phone")
    private String phone;
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式有误")
    @TableField("email")
    private String email;
    /**
     * 状态 0禁用 1正常
     */
    @TableField("status")
    private Integer status;
    /**
     * 在职状态 0离职 1在职
     */
    @TableField("on_job")
    private Integer onJob;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 显示顺序
     */
    @OrderBy(asc = true)
    @TableField("sort")
    private Integer sort;
    /**
     * 入职日期
     */
    @TableField("entry_date")
    private LocalDate entryDate;
    /**
     * 工号
     */
    @NotBlank(message = "工号不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("job_no")
    private String jobNo;
    /**
     * 部门ID
     */
    @TableField("dept_id")
    private Long deptId;
    /**
     * 职位ID
     */
    @TableField("position_id")
    private Long positionId;
    /**
     * 直属领导ID
     */
    @TableField("leader_id")
    private Long leaderId;
    /**
     * 逻辑删除 0未删除 NULL已删除
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
     * 角色ID列表
     */
    @TableField(exist = false)
    private List<Long> roleIds;

}

