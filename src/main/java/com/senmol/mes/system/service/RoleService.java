package com.senmol.mes.system.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.system.entity.RoleEntity;

/**
 * 角色(Role)表服务接口
 *
 * @author makejava
 * @since 2022-11-22 13:32:04
 */
public interface RoleService extends IService<RoleEntity> {
    /**
     * 通过主键查询单条数据(包括角色绑定的权限)
     *
     * @param id 主键
     * @return 单条数据
     */
    RoleEntity selectOne(Long id);

    /**
     * 检验用户是否是管理员权限
     *
     * @param userId 用户ID
     * @return 结果
     */
    boolean isAdmin(Long userId);

    /**
     * 新增数据
     *
     * @param role 实体对象
     * @return 新增结果
     */
    SaResult insertRole(RoleEntity role);

    /**
     * 修改数据
     *
     * @param role 实体对象
     * @return 修改结果
     */
    SaResult updateRole(RoleEntity role);

    /**
     * 删除数据(同时删除用户-角色表、角色-权限表数据)
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteRole(Long id);

}

