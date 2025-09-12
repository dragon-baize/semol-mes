package com.senmol.mes.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.entity.DeptEntity;
import com.senmol.mes.system.service.DeptService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 部门(Dept)表控制层
 *
 * @author makejava
 * @since 2023-01-11 09:01:18
 */
@Validated
@RestController
@RequestMapping("/system/dept")
public class DeptController {

    @Resource
    private DeptService deptService;

    /**
     * 查询所有部门
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        return SaResult.data(this.deptService.getList());
    }

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param dept 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<DeptEntity> page, DeptEntity dept) {
        return SaResult.data(this.deptService.page(page, new QueryWrapper<>(dept)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.deptService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param dept 实体对象
     * @return 新增结果
     */
    @Logger("部门新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody DeptEntity dept) {
        return this.deptService.insertDept(dept);
    }

    /**
     * 修改数据
     *
     * @param dept 实体对象
     * @return 修改结果
     */
    @Logger("部门修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody DeptEntity dept) {
        return this.deptService.updateDept(dept);
    }

    /**
     * 删除数据
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("部门删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.deptService.deleteDept(idList);
    }

}

