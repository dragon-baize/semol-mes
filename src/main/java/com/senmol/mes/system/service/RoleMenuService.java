package com.senmol.mes.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.system.entity.RoleMenuEntity;

import java.util.List;

/**
 * 角色-菜单(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-02-03 09:06:17
 */
public interface RoleMenuService extends IService<RoleMenuEntity> {

    /**
     * 保存角色-菜单数据
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    void saveRoleMenus(Long roleId, List<Long> menuIds);

}

