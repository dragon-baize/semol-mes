package com.senmol.mes.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.system.entity.UserRoleEntity;

import java.util.List;
import java.util.Set;

/**
 * 用户-角色(UserRole)表服务接口
 *
 * @author makejava
 * @since 2023-02-03 16:50:20
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    /**
     * 查询权限对应用户
     *
     * @param permId 权限ID
     * @return 用户ID列表
     */
    Set<Long> getUserIdByPermId(Long permId);

    /**
     * 查询菜单对应用户
     *
     * @param menuId 菜单ID
     * @return 用户ID列表
     */
    Set<Long> getUserIdByMenuId(Long menuId);

    /**
     * 保存用户-角色数据
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    void saveUserRole(Long userId, List<Long> roleIds);

}

