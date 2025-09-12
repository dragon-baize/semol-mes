package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.plan.vo.ProduceVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产计划(Produce)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
@Validated
@RestController
@RequestMapping("/plan/produce")
public class ProduceController {

    @Resource
    private ProduceService produceService;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param produce 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<ProduceVo> page, ProduceEntity produce) {
        return this.produceService.selectAll(page, produce);
    }

    /**
     * 查询产线计划产品
     *
     * @param productLineId 产线ID
     * @return 结果
     */
    @GetMapping("byProductLineId/{productLineId}")
    public SaResult byProductLineId(@PathVariable("productLineId") Long productLineId) {
        return SaResult.data(this.produceService.byProductLineId(productLineId));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.produceService.selectOne(id));
    }

    /**
     * 工作台统计
     */
    @GetMapping("workbench")
    public SaResult workbench() {
        return this.produceService.workbench();
    }

    /**
     * 新增数据
     *
     * @param produce 实体对象
     * @return 新增结果
     */
    @Logger("生产计划新增")
    @PostMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.ProduceEntity", table = "生产计划")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody ProduceEntity produce) {
        return this.produceService.insertProduce(produce);
    }

    /**
     * 批量新增
     *
     * @param produces 实体对象
     * @return 新增结果
     */
    @Logger("生产计划批量新增")
    @PostMapping("batch")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody List<ProduceEntity> produces) {
        return this.produceService.insertProduces(produces);
    }

    /**
     * 修改数据
     *
     * @param produce 实体对象
     * @return 修改结果
     */
    @Logger("生产计划修改")
    @PutMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.ProduceEntity", table = "生产计划")
    public SaResult update(@RequestBody ProduceEntity produce) {
        return this.produceService.updateProduce(produce);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("生产计划删除")
    @DeleteMapping
    @FlowCache(table = "生产计划", isAdd = false)
    public SaResult delete(@RequestParam("id") Long id) {
        return this.produceService.deleteProduce(id);
    }

}

