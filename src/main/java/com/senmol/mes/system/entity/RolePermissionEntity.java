package com.senmol.mes.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色权限(RolePermission)表实体类
 *
 * @author makejava
 * @since 2022-11-22 13:25:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_permission")
public class RolePermissionEntity extends Model<RolePermissionEntity> {
    private static final long serialVersionUID = -2433930606430008948L;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;
    /**
     * 权限ID
     */
    @TableField("permission_id")
    private Long permissionId;

}

