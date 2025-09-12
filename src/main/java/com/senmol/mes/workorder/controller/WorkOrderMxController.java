package com.senmol.mes.workorder.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.workorder.entity.WorkOrderMxEntity;
import com.senmol.mes.workorder.service.WorkOrderMxService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工单明细(WorkOrderMx)表控制层
 *
 * @author makejava
 * @since 2023-02-20 11:03:10
 */
@Validated
@RestController
@RequestMapping("workOrder/mx")
public class WorkOrderMxController {

    @Resource
    private WorkOrderMxService workOrderMxService;

    /**
     * 工单明细查询
     *
     * @param pid 主表ID
     * @return 所有数据
     */
    @GetMapping("getByPid/{pid}")
    public SaResult getByPid(@PathVariable("pid") Long pid) {
        return this.workOrderMxService.getByPid(pid);
    }

    /**
     * 查询工单明细单号
     *
     * @return 所有单号
     */
    @GetMapping("getMxCode")
    public SaResult getMxCode() {
        List<WorkOrderMxEntity> list = this.workOrderMxService.lambdaQuery()
                .eq(WorkOrderMxEntity::getStatus, 1)
                .eq(WorkOrderMxEntity::getFinish, 0)
                .orderByDesc(WorkOrderMxEntity::getCreateTime)
                .list();

        return SaResult.data(list);
    }

    /**
     * 工单查产品
     *
     * @param code 工单号
     * @return 产品信息
     */
    @GetMapping("getByCode/{code}")
    public SaResult getByCode(@PathVariable("code") String code) {
        return this.workOrderMxService.getByCode(code);
    }

    /**
     * 查询产线工单
     *
     * @param productLine 产线ID
     * @return 产线工单列表
     */
    @GetMapping("getLineOrder/{productLine}/{planId}")
    public SaResult getLineOrder(@PathVariable("productLine") Long productLine, @PathVariable("planId") Long planId) {
        return SaResult.data(this.workOrderMxService.getLineOrder(productLine, planId));
    }

    /**
     * 查询工单信息
     *
     * @param code 工单单号
     * @return 工位信息
     */
    @GetMapping("getStationInfo")
    public SaResult getStationInfo(@RequestParam String code) {
        return SaResult.data(this.workOrderMxService.getStationInfo(code));
    }

    /**
     * 查询工位物料
     *
     * @param code 工单单号
     * @return 工位信息
     */
    @GetMapping("getMaterials")
    public SaResult getMaterials(@RequestParam String code, @RequestParam Long stationId) {
        return SaResult.data(this.workOrderMxService.getMaterials(code, stationId));
    }

    /**
     * 正向追溯
     *
     * @param code 工单编号
     * @return 结果
     */
    @GetMapping("retrospect/{code}")
    public SaResult retrospect(@PathVariable("code") String code) {
        return SaResult.data(this.workOrderMxService.retrospect(code));
    }

    /**
     * 新增数据
     *
     * @param workOrderMx 实体对象
     * @return 新增结果
     */
    @Logger("工单新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody WorkOrderMxEntity workOrderMx) {
        return this.workOrderMxService.insert(workOrderMx);
    }

    /**
     * 批量新增
     *
     * @param workOrderMxs 实体对象
     * @return 新增结果
     */
    @Logger("工单批量新增")
    @PostMapping("batch")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody List<WorkOrderMxEntity> workOrderMxs) {
        return this.workOrderMxService.insertBatch(workOrderMxs);
    }

    /**
     * 查询工单工序
     */
    @GetMapping("processes/{mxCode}")
    public SaResult getProcesses(@PathVariable("mxCode") String mxCode) {
        return SaResult.data(this.workOrderMxService.getProcesses(mxCode));
    }

    /**
     * 修改数据
     *
     * @param workOrderMx 实体对象
     * @return 修改结果
     */
    @Logger("工单修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody WorkOrderMxEntity workOrderMx) {
        return this.workOrderMxService.updateWorkOrderMx(workOrderMx);
    }

    /**
     * 工单终止
     *
     * @param id 主键
     * @return 修改结果
     */
    @Logger("工单终止")
    @PutMapping("cutoff/{id}")
    public SaResult cutoff(@PathVariable("id") Long id) {
        return this.workOrderMxService.updateCutoff(id);
    }

    /**
     * 关单送检
     *
     * @param workOrderMx 实体对象
     * @return 更新结果
     */
    @Logger("工单关单送检")
    @PutMapping("submit")
    public SaResult submit(@Validated(ParamsValidate.Update.class) @RequestBody WorkOrderMxEntity workOrderMx) {
        return this.workOrderMxService.updateByIdAndSubmit(workOrderMx);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("工单删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.workOrderMxService.deleteWorkOrderMx(id);
    }

    /**
     * 获取产线下生产中的工单
     *
     * @param productLineId 产线ID
     * @return 生产中的工单
     */
    @GetMapping("getByLineId/{productLineId}")
    public SaResult getByLineId(@PathVariable("productLineId") Long productLineId) {
        return this.workOrderMxService.getByLineId(productLineId);
    }

    /**
     * 工作台
     */
    @GetMapping("workbench")
    public SaResult workbench(@RequestParam("productLineId") Long productLineId) {
        return this.workOrderMxService.workbench(productLineId);
    }

    /**
     * 工作台统计 周或月
     *
     * @param wom 0-周 1-月
     */
    @GetMapping("workbench/stat")
    public SaResult workbenchStat(@RequestParam("wom") Integer wom,
                                  @RequestParam("productLineId") Long productLineId) {
        return SaResult.data(this.workOrderMxService.workbenchStat(wom, productLineId));
    }

}

