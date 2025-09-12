package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.RequisitionEntity;
import com.senmol.mes.plan.page.RequisitionPage;
import com.senmol.mes.plan.service.RequisitionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 请购单(Requisition)表控制层
 *
 * @author makejava
 * @since 2023-03-13 15:57:22
 */
@Validated
@RestController
@RequestMapping("/plan/requisition")
public class RequisitionController {

    @Resource
    private RequisitionService requisitionService;

    /**
     * 分页查询所有数据，size=-1查询所有数据
     * 实体类
     *
     * @param page        分页对象
     * @param requisition 查询实体
     * @param keyword     关键字
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(RequisitionPage page, RequisitionEntity requisition, String keyword) {
        return this.requisitionService.selectAll(page, requisition, keyword);
    }

    /**
     * 获取未绑定的请购单
     *
     * @return 列表
     */
    @GetMapping("getUnbound")
    public SaResult getUnbound() {
        return SaResult.data(this.requisitionService.getUnbound());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.requisitionService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param requisition 实体对象
     * @return 新增结果
     */
    @Logger("请购单新增")
    @PostMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.RequisitionEntity", table = "请购单")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody RequisitionEntity requisition) {
        return this.requisitionService.saveRequisition(requisition);
    }

    /**
     * 批量新增
     *
     * @param requisitions 实体对象列表
     * @return 新增结果
     */
    @Logger("请购单批量新增")
    @PostMapping("batch")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody List<RequisitionEntity> requisitions) {
        return this.requisitionService.saveRequisitions(requisitions);
    }

    /**
     * 修改数据
     *
     * @param requisition 实体对象
     * @return 修改结果
     */
    @Logger("请购单修改")
    @PutMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.RequisitionEntity", table = "请购单")
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody RequisitionEntity requisition) {
        return this.requisitionService.updateRequisition(requisition);
    }

    /**
     * 删除数据
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("请购单删除")
    @DeleteMapping
    @FlowCache(table = "请购单", isAdd = false)
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.requisitionService.removeRequisition(idList);
    }
}

