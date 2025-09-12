package com.senmol.mes.quality.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.quality.entity.BadModeEntity;
import com.senmol.mes.quality.service.BadModeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 不良模式(BadMode)表控制层
 *
 * @author makejava
 * @since 2023-01-31 09:15:52
 */
@RestController
@RequestMapping("/quality/badMode")
public class BadModeController {

    @Resource
    private BadModeService badModeService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList(BadModeEntity badMode) {
        return SaResult.data(this.badModeService.lambdaQuery(badMode).list());
    }

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param badMode 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<BadModeEntity> page, BadModeEntity badMode) {
        return this.badModeService.selectAll(page, badMode);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.badModeService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param badMode 实体对象
     * @return 新增结果
     */
    @Logger("不良模式新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody BadModeEntity badMode) {
        return this.badModeService.insertBadMode(badMode);
    }

    /**
     * 修改数据
     *
     * @param badMode 实体对象
     * @return 修改结果
     */
    @Logger("不良模式修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody BadModeEntity badMode) {
        return this.badModeService.updateBadMode(badMode);
    }

    /**
     * 删除数据
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("不良模式删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.badModeService.delBadMode(idList);
    }

}

