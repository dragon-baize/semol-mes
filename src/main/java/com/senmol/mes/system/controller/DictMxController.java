package com.senmol.mes.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.entity.DictMxEntity;
import com.senmol.mes.system.service.DictMxService;
import com.senmol.mes.system.utils.SysAsyncUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典明细(DictMx)表控制层
 *
 * @author makejava
 * @since 2023-01-03 17:05:23
 */
@RestController
@RequestMapping("/system/dictMx")
public class DictMxController {

    @Resource
    private DictMxService dictMxService;
    @Resource
    private SysAsyncUtil sysAsyncUtil;

    /**
     * 分页查询所有数据
     *
     * @param page   分页对象
     * @param dictMx 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<DictMxEntity> page, DictMxEntity dictMx) {
        return SaResult.data(this.dictMxService.page(page, new QueryWrapper<>(dictMx)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.dictMxService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param dictMx 实体对象
     * @return 新增结果
     */
    @Logger("字典明细新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody DictMxEntity dictMx) {
        boolean isExist = this.titleIsExist(dictMx);
        if (isExist) {
            return SaResult.error("字典明细名称重复");
        }

        this.dictMxService.save(dictMx);
        // 添加到缓存中
        this.sysAsyncUtil.dealDictMx(dictMx);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    /**
     * 修改数据
     *
     * @param dictMx 实体对象
     * @return 修改结果
     */
    @Logger("字典明细修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody DictMxEntity dictMx) {
        boolean isExist = this.titleIsExist(dictMx);
        if (isExist) {
            return SaResult.error("字典明细名称重复");
        }

        this.dictMxService.updateById(dictMx);
        // 添加到缓存中
        this.sysAsyncUtil.dealDictMx(dictMx);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("字典明细删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") List<Long> idList) {
        this.dictMxService.removeByIds(idList);

        // 删除缓存中数据
        this.sysAsyncUtil.delDictMx(idList);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private boolean titleIsExist(DictMxEntity dictMx) {
        List<DictMxEntity> list = this.dictMxService.lambdaQuery()
                .eq(DictMxEntity::getTitle, dictMx.getTitle())
                .eq(DictMxEntity::getPid, dictMx.getPid())
                .list();
        if (dictMx.getId() != null) {
            list = list.stream()
                    .filter(item -> !item.getId().equals(dictMx.getId()))
                    .collect(Collectors.toList());
        }

        return list.size() > 0;
    }
}

