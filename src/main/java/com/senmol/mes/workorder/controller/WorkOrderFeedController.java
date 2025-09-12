package com.senmol.mes.workorder.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.workorder.entity.WorkOrderFeedEntity;
import com.senmol.mes.workorder.page.Retrospect;
import com.senmol.mes.workorder.service.WorkOrderFeedService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 上料记录(WorkOrderFeed)表控制层
 *
 * @author makejava
 * @since 2023-10-23 10:44:08
 */
@Validated
@RestController
@RequestMapping("workOrder/feed")
public class WorkOrderFeedController {

    @Resource
    private WorkOrderFeedService workOrderFeedService;

    /**
     * 上料记录
     *
     * @param mxId       工单ID
     * @param stationId  工位ID
     * @param materialId 物料ID
     * @return 结果
     */
    @GetMapping
    public SaResult getByMxId(@RequestParam("mxId") Long mxId, @RequestParam("stationId") Long stationId,
                              @RequestParam("materialId") Long materialId) {
        return this.workOrderFeedService.getByMxId(mxId, stationId, materialId);
    }

    /**
     * 原材料用量追溯
     *
     * @param retrospect 查询对象
     * @return 结果
     */
    @GetMapping("retrospect")
    public SaResult retrospect(Retrospect retrospect) {
        return this.workOrderFeedService.retrospect(retrospect);
    }

    /**
     * 新增数据
     *
     * @param workOrderFeed 实体对象
     * @return 新增结果
     */
    @Logger("上料记录新增")
    @PostMapping
    public SaResult insert(@RequestBody WorkOrderFeedEntity workOrderFeed) {
        return this.workOrderFeedService.insertFeed(workOrderFeed);
    }

    /**
     * 删除记录
     */
    @Logger("上料记录删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        this.workOrderFeedService.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

