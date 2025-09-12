package com.senmol.mes.workorder.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.workorder.entity.WorkOrderMxMaterialEntity;
import com.senmol.mes.workorder.vo.OrderMaterial;

import java.util.List;
import java.util.Set;

/**
 * 工单明细物料(WorkOrderMxMaterial)表服务接口
 *
 * @author makejava
 * @since 2023-02-21 10:54:03
 */
public interface WorkOrderMxMaterialService extends IService<WorkOrderMxMaterialEntity> {

    /**
     * 工单明细查物料
     *
     * @param workOrderMxId 工单明细ID
     * @return 物料列表
     */
    List<WorkOrderMxMaterialEntity> getByMxId(Long workOrderMxId);

    /**
     * 工单明细查物料
     *
     * @param mxCode 工单明细编号
     * @return 物料列表
     */
    SaResult getByMxCode(String mxCode);

    /**
     * 产品查工单物料
     *
     * @param productId 产品
     * @param id        生产计划ID
     * @param mxId      子工单ID
     * @return 工单物料列表
     */
    List<OrderMaterial> getMaterials(Long productId, Long id, Long mxId);

    /**
     * 工单明细ID查领取的物料
     *
     * @param mxId 工单明细ID
     * @return 物料列表
     */
    List<OrderMaterial> selectByMxId(Long mxId);

    /**
     * 指定物料的预占用库存
     *
     * @param materialId 物料ID
     * @return 预占用库存
     */
    SaResult preInventory(Long materialId);

    /**
     * 查询物料占用库存
     *
     * @param ids 物料ID列表
     * @return 占用库存
     */
    List<CountVo> getMaterial(Set<Long> ids);

    /**
     * 反向追溯
     *
     * @param batchNo 入库批次号
     * @return 结果
     */
    SaResult retrospect(String batchNo);

    /**
     * 批量新增数据
     *
     * @param workOrderMxMaterials 实体对象列表
     * @return 新增结果
     */
    SaResult insertBatch(List<WorkOrderMxMaterialEntity> workOrderMxMaterials);

}

