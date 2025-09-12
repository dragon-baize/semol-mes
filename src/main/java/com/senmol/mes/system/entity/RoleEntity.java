package com.senmol.mes.system.entity;

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
 * 角色(Role)表实体类
 *
 * @author makejava
 * @since 2022-11-22 13:25:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class RoleEntity extends Model<RoleEntity> {
    private static final long serialVersionUID = -7314233759501862598L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "角色名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField("title")
    private String title;
    /**
     * 编号
     */
    @NotBlank(message = "角色编号不能为空", groups = ParamsValidate.Insert.class)
    @TableField("code")
    private String code;
    /**
     * 状态 0禁用 1正常
     */
    @TableField("status")
    private Integer status;
    /**
     * 逻辑删除 0未删除 NULL已删除
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 创建时间
     */
    @OrderBy
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 版本号
     */
    @TableField("version")
    @Version
    private Integer version;
    /**
     * 权限ID列表
     */
    @TableField(exist = false)
    private List<Long> permissionIds;
    /**
     * 菜单ID列表
     */
    @TableField(exist = false)
    private List<Long> menuIds;

}

