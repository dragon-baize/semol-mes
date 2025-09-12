package com.senmol.mes.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.entity.PermissionEntity;
import com.senmol.mes.system.service.PermissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 权限(Permission)表控制层
 * 开放接口所有人都能权限访问
 * 添加的开放接口地址必须是唯一的
 *
 * @author makejava
 * @since 2022-11-22 13:33:03
 */
@RestController
@RequestMapping("/system/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        return this.permissionService.getList();
    }

    /**
     * 分页查询所有数据
     *
     * @param page       分页对象
     * @param permission 查询实体
     * @return 所有数据
     */
    @SuppressWarnings("unchecked")
    @GetMapping
    public SaResult selectAll(Page<PermissionEntity> page, PermissionEntity permission) {
        LambdaQueryWrapper<PermissionEntity> wrapper = new LambdaQueryWrapper<>(permission);
        wrapper.orderByAsc(
                PermissionEntity::getCatalogue,
                PermissionEntity::getPage,
                PermissionEntity::getPageSort,
                PermissionEntity::getButtonSort
        );
        return SaResult.data(this.permissionService.page(page, wrapper));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.permissionService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param permission 实体对象
     * @return 新增结果
     */
    @Logger("权限新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody PermissionEntity permission) {
        return this.permissionService.insertPermission(permission);
    }

    /**
     * 修改数据
     *
     * @param permission 实体对象
     * @return 修改结果
     */
    @Logger("权限修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody PermissionEntity permission) {
        return this.permissionService.updatePermission(permission);
    }

    /**
     * 删除数据(同时删除菜单-权限表、角色-权限表数据)
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("权限删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.permissionService.deletePermission(idList);
    }

}

