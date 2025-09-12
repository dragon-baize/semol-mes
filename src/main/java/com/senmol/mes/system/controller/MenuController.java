package com.senmol.mes.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.entity.MenuEntity;
import com.senmol.mes.system.service.MenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 菜单(Menu)表控制层
 *
 * @author makejava
 * @since 2022-11-22 13:33:03
 */
@Validated
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        return SaResult.data(this.menuService.getList());
    }

    /**
     * 通过主键查询单条数据(包括菜单绑定的权限)
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable Long id) {
        return SaResult.data(this.menuService.getById(id));
    }

    /**
     * 查询用户菜单
     *
     * @param userId 用户ID
     * @return 菜单树列表
     */
    @GetMapping("user/{userId}")
    public SaResult getByUserId(@PathVariable("userId") Long userId) {
        return SaResult.data(this.menuService.getByUserId(userId));
    }

    /**
     * 新增数据
     *
     * @param menu 实体对象
     * @return 新增结果
     */
    @Logger("菜单新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody MenuEntity menu) {
        return this.menuService.insertMenu(menu);
    }

    /**
     * 修改数据
     *
     * @param menu 实体对象
     * @return 修改结果
     */
    @Logger("菜单修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody MenuEntity menu) {
        return this.menuService.updateMenu(menu);
    }

    /**
     * 删除数据(同时删除角色-菜单数据)
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("菜单删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.menuService.deleteMenu(id);
    }

}

