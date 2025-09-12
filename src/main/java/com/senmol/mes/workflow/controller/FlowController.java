package com.senmol.mes.workflow.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.workflow.entity.Flow;
import com.senmol.mes.workflow.service.FlowService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 审批流程(Flow)表控制层
 *
 * @author makejava
 * @since 2023-03-24 14:25:45
 */
@RestController
@RequestMapping("/work/flow")
public class FlowController {

    @Resource
    private FlowService flowService;

    /**
     * 通过菜单ID查询单条数据
     *
     * @param title 菜单
     * @return 单条数据
     */
    @GetMapping("getByTitle/{title}")
    public SaResult getByMenuId(@PathVariable("title") String title) {
        return this.flowService.getByTitle(title);
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        return SaResult.data(this.flowService.getList());
    }

    /**
     * 新增数据
     *
     * @param flow 实体对象
     * @return 新增结果
     */
    @Logger("审批流程新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody Flow flow) {
        return this.flowService.insertFlow(flow);
    }

    /**
     * 修改数据
     *
     * @param flow 实体对象
     * @return 修改结果
     */
    @Logger("审批流程修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody Flow flow) {
        return this.flowService.updateFlow(flow);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("审批流程删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.flowService.deleteFlow(id);
    }
}

