package com.senmol.mes.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.system.entity.RolePermissionEntity;

import java.util.List;

/**
 * 角色权限(RolePermission)表服务接口
 *
 * @author makejava
 * @since 2022-11-22 13:32:04
 */
public interface RolePermissionService extends IService<RolePermissionEntity> {
    /**
     * 新增角色权限
     *
     * @param roleId        角色id
     * @param permissionIds 权限ID列表
     */
    void saveRolePerms(Long roleId, List<Long> permissionIds);
}

