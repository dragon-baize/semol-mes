package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.produce.entity.PackEntity;
import com.senmol.mes.produce.service.PackService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 包装管理(Pack)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:00:13
 */
@RestController
@RequestMapping("/produce/pack")
public class PackController {

    @Resource
    private PackService packService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        return SaResult.data(this.packService.list());
    }

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param pack 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<PackEntity> page, PackEntity pack) {
        return this.packService.selectAll(page, pack);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.packService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param pack 实体对象
     * @return 新增结果
     */
    @Logger("包装新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody PackEntity pack) {
        return this.packService.insertPack(pack);
    }

    /**
     * 修改数据
     *
     * @param pack 实体对象
     * @return 修改结果
     */
    @Logger("包装修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody PackEntity pack) {
        return this.packService.updatePack(pack);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("包装删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.packService.deletePack(idList);
    }
}

