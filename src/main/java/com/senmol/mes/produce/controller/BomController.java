package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.produce.entity.BomEntity;
import com.senmol.mes.produce.service.BomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 物料清单(Bom)表控制层
 *
 * @author makejava
 * @since 2023-01-29 14:59:11
 */
@RestController
@RequestMapping("/produce/bom")
public class BomController {

    @Resource
    private BomService bomService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param bom  查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<BomEntity> page, BomEntity bom) {
        return this.bomService.selectAll(page, bom);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return this.bomService.selectOne(id);
    }

    /**
     * 新增数据
     *
     * @param bom 实体对象
     * @return 新增结果
     */
    @Logger("物料清单新增")
    @PostMapping
    @FlowCache(entity = "com.senmol.mes.produce.entity.BomEntity", table = "物料清单")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody BomEntity bom) {
        return this.bomService.insertBom(bom);
    }

    /**
     * 修改数据
     *
     * @param bom 实体对象
     * @return 修改结果
     */
    @Logger("物料清单修改")
    @PutMapping
    @FlowCache(entity = "com.senmol.mes.produce.entity.BomEntity", table = "物料清单")
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody BomEntity bom) {
        return this.bomService.updateBom(bom);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("物料清单删除")
    @DeleteMapping
    @FlowCache(table = "物料清单", isAdd = false)
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.bomService.deleteBom(idList);
    }
}

