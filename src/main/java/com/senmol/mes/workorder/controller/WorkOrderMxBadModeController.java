package com.senmol.mes.workorder.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.quality.vo.BadModeVo;
import com.senmol.mes.workorder.entity.WorkOrderMxBadMode;
import com.senmol.mes.workorder.service.WorkOrderMxBadModeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * (WorkOrderMxBadMode)表控制层
 *
 * @author makejava
 * @since 2024-02-26 16:06:21
 */
@RestController
@RequestMapping("workOrder/mx/badMode")
public class WorkOrderMxBadModeController {

    @Resource
    private WorkOrderMxBadModeService workOrderMxBadModeService;
    @Resource
    private ProFromRedis proFromRedis;

    @GetMapping
    public SaResult getList(@RequestParam("mxId") Long mxId, @RequestParam("processId") Long processId) {
        List<WorkOrderMxBadMode> list = this.workOrderMxBadModeService.lambdaQuery()
                .eq(WorkOrderMxBadMode::getMxId, mxId)
                .eq(WorkOrderMxBadMode::getProcessId, processId)
                .list();

        for (WorkOrderMxBadMode badMode : list) {
            BadModeVo mode = this.proFromRedis.getBadMode(badMode.getBadModeId());
            if (ObjUtil.isNotNull(mode)) {
                badMode.setBadModeTitle(mode.getTitle());
            }
        }

        return SaResult.data(list);
    }
}

