package com.senmol.mes.workorder.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.plan.vo.ProduceVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工单管理(WorkOrder)表控制层
 *
 * @author makejava
 * @since 2023-02-20 09:51:46
 */
@Validated
@RestController
@RequestMapping("workOrder")
public class WorkOrderController {

    @Resource
    private ProduceService produceService;

    /**
     * 分页查询
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
     * 产品在制生产量
     *
     * @param productIds 产品ID列表
     * @return 结果
     */
    @GetMapping("inProcess")
    public SaResult inProcess(@RequestParam("productIds") List<Long> productIds) {
        return this.produceService.inProcess(productIds);
    }

    /**
     * 设置计划完成，关闭所有工单
     *
     * @param produce 实体对象
     * @return 处理结果
     */
    @Logger("任务单关闭")
    @PutMapping
    public SaResult closeOrder(@RequestBody ProduceEntity produce) {
        return this.produceService.closeOrder(produce);
    }

}

