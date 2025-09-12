package com.senmol.mes.workorder.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.workorder.entity.WorkOrderMxMaterialEntity;
import com.senmol.mes.workorder.service.WorkOrderMxMaterialService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工单明细物料(WorkOrderMxMaterial)表控制层
 *
 * @author makejava
 * @since 2023-02-21 10:54:03
 */
@RestController
@RequestMapping("workOrder/mx/material")
public class WorkOrderMxMaterialController {

    @Resource
    private WorkOrderMxMaterialService workOrderMxMaterialService;

    /**
     * 工单明细查物料
     *
     * @param workOrderMxId 工单明细ID
     * @return 物料列表
     */
    @GetMapping("getByMxId/{workOrderMxId}")
    public SaResult getByMxId(@PathVariable("workOrderMxId") Long workOrderMxId) {
        return SaResult.data(this.workOrderMxMaterialService.getByMxId(workOrderMxId));
    }

    /**
     * 工单明细查物料
     *
     * @param mxCode 工单明细编号
     * @return 物料列表
     */
    @GetMapping("getByMxCode/{mxCode}")
    public SaResult getByMxCode(@PathVariable("mxCode") String mxCode) {
        return this.workOrderMxMaterialService.getByMxCode(mxCode);
    }

    /**
     * 产品查工单物料
     *
     * @param productId 产品
     * @param id        生产计划ID
     * @param mxId      子工单ID
     * @return 工单物料列表
     */
    @GetMapping("/getMaterials")
    public SaResult getMaterials(@RequestParam("productId") Long productId,
                                 @RequestParam("id") Long id,
                                 @RequestParam(value = "mxId", required = false) Long mxId) {
        return SaResult.data(this.workOrderMxMaterialService.getMaterials(productId, id, mxId));
    }

    /**
     * 工单明细ID查领取的物料
     *
     * @param mxId 工单明细ID
     * @return 物料列表
     */
    @GetMapping("selectByMxId/{mxId}")
    public SaResult selectByMxId(@PathVariable("mxId") Long mxId) {
        return SaResult.data(this.workOrderMxMaterialService.selectByMxId(mxId));
    }

    /**
     * 指定物料的预占用库存
     *
     * @param materialId 物料ID
     * @return 预占用库存
     */
    @GetMapping("preInventory/{materialId}")
    public SaResult preInventory(@PathVariable("materialId") Long materialId) {
        return this.workOrderMxMaterialService.preInventory(materialId);
    }

    /**
     * 反向追溯
     *
     * @param batchNo 入库批次号
     * @return 结果
     */
    @GetMapping("retrospect/{batchNo}")
    public SaResult nRetrospect(@PathVariable("batchNo") String batchNo) {
        return this.workOrderMxMaterialService.retrospect(batchNo);
    }

    /**
     * 工单释放物料保存
     *
     * @param workOrderMxMaterials 实体对象列表
     * @return 新增结果
     */
    @Logger("工单物料批量新增")
    @PostMapping
    public SaResult insertBatch(@Validated(ParamsValidate.Insert.class) @RequestBody List<WorkOrderMxMaterialEntity> workOrderMxMaterials) {
        return this.workOrderMxMaterialService.insertBatch(workOrderMxMaterials);
    }

}

