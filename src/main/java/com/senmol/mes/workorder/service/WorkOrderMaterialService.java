package com.senmol.mes.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.produce.vo.BomMaterialVo;
import com.senmol.mes.workorder.entity.WorkOrderMaterial;

import java.util.List;

/**
 * 任务单物料(WorkOrderMaterial)表服务接口
 *
 * @author makejava
 * @since 2023-11-07 13:39:58
 */
public interface WorkOrderMaterialService extends IService<WorkOrderMaterial> {

    /**
     * 查询工单工位工序
     *
     * @param mxId 工单ID
     * @return 结果
     */
    List<WorkOrderMaterial> getByMxId(Long mxId);

    /**
     * 获取工单对应物料及其基础使用量
     *
     * @param mxId      工单ID
     * @param processId 工序ID
     * @return 结果
     */
    List<CountVo> getBaseMaterial(Long mxId, Long processId);

    /**
     * 批量保存
     *
     * @param mxId     工单ID
     * @param entities 物料清单数据
     * @return 受影响行数
     */
    int insertBatch(Long mxId, List<BomMaterialVo> entities);

}

