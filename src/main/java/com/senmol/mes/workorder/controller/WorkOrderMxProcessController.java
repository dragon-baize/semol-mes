package com.senmol.mes.workorder.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.workorder.entity.WorkOrderMxProcess;
import com.senmol.mes.workorder.service.WorkOrderMxProcessService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工单工序(WorkOrderMxProcess)表控制层
 *
 * @author makejava
 * @since 2023-11-23 14:29:45
 */
@RestController
@RequestMapping("workOrder/mx/process")
public class WorkOrderMxProcessController {

    @Resource
    private WorkOrderMxProcessService workOrderMxProcessService;

    @GetMapping
    public SaResult getList(@RequestParam("mxId") Long mxId, @RequestParam("processId") Long processId) {
        List<WorkOrderMxProcess> list = this.workOrderMxProcessService.lambdaQuery()
                .eq(WorkOrderMxProcess::getMxId, mxId)
                .eq(WorkOrderMxProcess::getProcessId, processId)
                .list();

        return SaResult.data(list);
    }

    /**
     * 查询工单明细
     *
     * @param mxId 工单ID
     * @return 工单明细
     */
    @GetMapping("getByMxId/{mxId}")
    public SaResult getByMxId(@PathVariable("mxId") Long mxId) {
        return SaResult.data(this.workOrderMxProcessService.getByMxId(mxId));
    }

    /**
     * 开工
     *
     * @param process 实体对象
     * @return 修改结果
     */
    @Logger("开工")
    @PostMapping
    public SaResult start(@Validated(ParamsValidate.Insert.class) @RequestBody WorkOrderMxProcess process) {
        return this.workOrderMxProcessService.start(process);
    }

    /**
     * 取消开工
     */
    @Logger("取消开工")
    @PutMapping("cancel")
    public SaResult cancel(@RequestParam Long mxId, @RequestParam Long stationId, @RequestParam Long processId) {
        return this.workOrderMxProcessService.cancel(mxId, stationId, processId);
    }

    /**
     * 报工
     *
     * @param process 实体对象
     * @return 修改结果
     */
    @Logger("报工")
    @PutMapping
    public SaResult end(@Validated(ParamsValidate.Update.class) @RequestBody WorkOrderMxProcess process) {
        return this.workOrderMxProcessService.end(process);
    }

}

