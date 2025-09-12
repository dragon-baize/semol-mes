package com.senmol.mes.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.entity.RoleEntity;
import com.senmol.mes.system.service.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色(Role)表控制层
 *
 * @author makejava
 * @since 2022-11-22 13:33:03
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        List<RoleEntity> list = this.roleService.lambdaQuery().eq(RoleEntity::getStatus, 1).list();
        return SaResult.data(list);
    }

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param role 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<RoleEntity> page, RoleEntity role) {
        return SaResult.data(this.roleService.page(page, new QueryWrapper<>(role)));
    }

    /**
     * 通过主键查询单条数据(包括角色绑定的权限、菜单)
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable Long id) {
        return SaResult.data(this.roleService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param role 实体对象
     * @return 新增结果
     */
    @Logger("角色新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody RoleEntity role) {
        return this.roleService.insertRole(role);
    }

    /**
     * 修改数据
     *
     * @param role 实体对象
     * @return 修改结果
     */
    @Logger("角色修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody RoleEntity role) {
        return this.roleService.updateRole(role);
    }

    /**
     * 删除数据(同时删除用户-角色、角色-权限、角色-菜单数据)
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("角色删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.roleService.deleteRole(id);
    }

}

