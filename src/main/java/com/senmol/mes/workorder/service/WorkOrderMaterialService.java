package com.senmol.mes.workorder.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.vo.BomMaterialVo;
import com.senmol.mes.workorder.entity.WorkOrderMaterial;

import java.math.BigDecimal;
import java.util.List;

/**
 * 任务单物料(WorkOrderMaterial)表服务接口
 *
 * @author makejava
 * @since 2023-11-07 13:39:58
 */
public interface WorkOrderMaterialService extends IService<WorkOrderMaterial> {

    /**
     * 工单ID查询任务单物料工序
     */
    List<WorkOrderMaterial> getByMxId(Long mxId);

    /**
     * 工序物料基础使用量
     */
    List<Dict> getBaseMaterial(Long mxId, Long processId, BigDecimal count);

    /**
     * 批量保存
     *
     * @param mxId     工单ID
     * @param entities 物料清单数据
     * @return 受影响行数
     */
    int insertBatch(Long mxId, List<BomMaterialVo> entities);

}

