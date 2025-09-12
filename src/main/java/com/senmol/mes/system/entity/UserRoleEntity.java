package com.senmol.mes.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户-角色(UserRole)表实体类
 *
 * @author makejava
 * @since 2023-02-03 16:50:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_role")
public class UserRoleEntity extends Model<UserRoleEntity> {
    private static final long serialVersionUID = -3549743727617179722L;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

}

