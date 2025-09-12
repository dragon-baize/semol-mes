package com.senmol.mes.system.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.system.entity.RoleEntity;
import com.senmol.mes.system.entity.RoleMenuEntity;
import com.senmol.mes.system.entity.RolePermissionEntity;
import com.senmol.mes.system.mapper.RoleMapper;
import com.senmol.mes.system.service.RoleMenuService;
import com.senmol.mes.system.service.RolePermissionService;
import com.senmol.mes.system.service.RoleService;
import com.senmol.mes.system.utils.SysAsyncUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-11-22 13:32:04
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Resource
    private RolePermissionService rolePermissionService;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private SysAsyncUtil sysAsyncUtil;
    @Value("${login.type.station}")
    private String station;

    @Override
    public RoleEntity selectOne(Long id) {
        // 获取角色信息
        RoleEntity role = this.getById(id);

        // 获取角色-权限数据
        List<Long> permissionIds = this.rolePermissionService.lambdaQuery()
                .eq(RolePermissionEntity::getRoleId, id)
                .list()
                .stream()
                .map(RolePermissionEntity::getPermissionId)
                .collect(Collectors.toList());
        role.setPermissionIds(permissionIds);

        // 获取角色-菜单数据
        List<Long> menuIds = this.roleMenuService.lambdaQuery()
                .eq(RoleMenuEntity::getRoleId, id)
                .list()
                .stream()
                .map(RoleMenuEntity::getMenuId)
                .collect(Collectors.toList());
        role.setMenuIds(menuIds);

        return role;
    }

    @Override
    public boolean isAdmin(Long userId) {
        return this.baseMapper.isAdmin(userId) > 0;
    }

    @Override
    public SaResult insertRole(RoleEntity role) {
        String checkData = this.checkData(role);
        if (StrUtil.isNotBlank(checkData)) {
            return SaResult.error(checkData);
        }

        // 获取权限ID
        List<Long> permissionIds = role.getPermissionIds();
        // 获取菜单ID
        List<Long> menuIds = role.getMenuIds();

        // 保存角色数据
        this.save(role);

        Long id = role.getId();
        // 保存角色-权限数据
        if (id != null && CollUtil.isNotEmpty(permissionIds)) {
            this.rolePermissionService.saveRolePerms(role.getId(), permissionIds);
        }
        // 保存角色-菜单数据
        if (id != null && CollUtil.isNotEmpty(menuIds)) {
            this.roleMenuService.saveRoleMenus(role.getId(), menuIds);
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateRole(RoleEntity role) {
        String checkData = this.checkData(role);
        if (StrUtil.isNotBlank(checkData)) {
            return SaResult.error(checkData);
        }

        // 获取权限ID
        List<Long> permissionIds = role.getPermissionIds();
        // 获取菜单ID
        List<Long> menuIds = role.getMenuIds();

        this.updateById(role);
        // 删除角色-权限数据
        this.rolePermissionService.lambdaUpdate().eq(RolePermissionEntity::getRoleId, role.getId()).remove();
        // 删除角色-菜单数据
        this.roleMenuService.lambdaUpdate().eq(RoleMenuEntity::getRoleId, role.getId()).remove();

        // 保存角色-权限数据
        if (CollUtil.isNotEmpty(permissionIds)) {
            this.rolePermissionService.saveRolePerms(role.getId(), permissionIds);
        }
        // 保存角色-菜单数据
        if (CollUtil.isNotEmpty(menuIds)) {
            this.roleMenuService.saveRoleMenus(role.getId(), menuIds);
        }

        // 处理用户权限数据
        if (station.equals(role.getCode())) {
            this.sysAsyncUtil.dealRole(role.getId());
        } else {
            this.sysAsyncUtil.dealRole(role.getId(), 0);
        }
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteRole(Long id) {
        // 删除角色-权限数据
        this.rolePermissionService.lambdaUpdate().eq(RolePermissionEntity::getRoleId, id).remove();
        // 删除角色-菜单数据
        this.roleMenuService.lambdaUpdate().eq(RoleMenuEntity::getRoleId, id).remove();

        this.removeById(id);
        // 处理用户权限数据
        this.sysAsyncUtil.dealRole(id, 1);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    /**
     * 校验角色名称、编号
     */
    private String checkData(RoleEntity role) {
        List<RoleEntity> list = this.list();

        // 校验名称
        boolean titleMatch;
        if (role.getId() != null) {
            titleMatch = list.stream().anyMatch(item ->
                    !item.getId().equals(role.getId()) && item.getTitle().equals(role.getTitle())
            );
        } else {
            titleMatch = list.stream().anyMatch(item -> item.getTitle().equals(role.getTitle()));
        }

        if (titleMatch) {
            return "角色名称已存在";
        }

        // 校验编号
        boolean codeMatch;
        if (role.getId() != null) {
            codeMatch = list.stream().anyMatch(item ->
                    !item.getId().equals(role.getId()) && item.getCode().equals(role.getCode())
            );
        } else {
            codeMatch = list.stream().anyMatch(item -> item.getCode().equals(role.getCode()));
        }

        if (codeMatch) {
            return "角色编号已存在";
        }

        return null;
    }
}
