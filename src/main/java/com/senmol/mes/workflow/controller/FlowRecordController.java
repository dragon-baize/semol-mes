package com.senmol.mes.workflow.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.workflow.entity.FlowRecord;
import com.senmol.mes.workflow.service.FlowRecordService;
import com.senmol.mes.workflow.vo.FlowRecordVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 审核记录(FlowRecord)表控制层
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
@Validated
@RestController
@RequestMapping("/work/flow/record")
public class FlowRecordController {

    @Resource
    private FlowRecordService flowRecordService;

    /**
     * 分页查询所有数据，传入项目主键(itemId)时为查询单条数据
     *
     * @param page       分页对象
     * @param flowRecord 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<FlowRecordVo> page, FlowRecord flowRecord) {
        // 项目主键查询
        if (ObjectUtil.isNotNull(flowRecord.getId())) {
            return SaResult.data(this.flowRecordService.selectOne(flowRecord.getId()));
        }

        // 分页
        return SaResult.data(this.flowRecordService.selectAll(page, flowRecord));
    }

    /**
     * 项目ID查询
     *
     * @param itemId 项目ID
     * @return 审批信息
     */
    @GetMapping("{itemId}")
    public SaResult getByItemId(@PathVariable("itemId") Long itemId) {
        return SaResult.data(
                this.flowRecordService.lambdaQuery()
                        .eq(FlowRecord::getItemId, itemId)
                        .one()
        );
    }

}

