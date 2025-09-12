package com.senmol.mes.quality.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.quality.entity.StorageInspectEntity;
import com.senmol.mes.quality.service.StorageInspectService;
import com.senmol.mes.quality.vo.StorageInspectVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 入库检测(StorageInspect)表控制层
 *
 * @author makejava
 * @since 2023-01-31 09:45:22
 */
@RestController
@RequestMapping("/quality/storage/inspect")
public class StorageInspectController {

    @Resource
    private StorageInspectService storageInspectService;

    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param storageInspect 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<StorageInspectVo> page, StorageInspectEntity storageInspect) {
        if (ObjectUtil.isNull(storageInspect.getType())) {
            return SaResult.error("缺少类型");
        }

        return SaResult.data(this.storageInspectService.selectAll(page, storageInspect));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.storageInspectService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param storageInspect 实体对象
     * @return 新增结果
     */
    @Logger("入库检测新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody StorageInspectEntity storageInspect) {
        return this.storageInspectService.insertStorageInspect(storageInspect);
    }

    /**
     * 修改数据
     *
     * @param storageInspect 实体对象
     * @return 修改结果
     */
    @Logger("入库检测修改")
    @PutMapping
    public SaResult update(@RequestBody StorageInspectEntity storageInspect) {
        return this.storageInspectService.updateStorageInspect(storageInspect);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("入库检测删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        this.storageInspectService.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    /**
     * 待入库退回
     */
    @Logger("入库检测退回")
    @PutMapping("back/{id}")
    public SaResult back(@PathVariable("id") Long id) {
        return this.storageInspectService.back(id);
    }

}

