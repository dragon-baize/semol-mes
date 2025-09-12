package com.senmol.mes.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.MrpMaterial;
import com.senmol.mes.workorder.vo.MaterialPojo;

import java.util.List;

/**
 * MRP物料计算结果(PlanMrpMaterial)表服务接口
 *
 * @author makejava
 * @since 2023-11-14 13:09:46
 */
public interface MrpMaterialService extends IService<MrpMaterial> {
    /**
     * 查询物料预占用库存
     *
     * @param materialId 物料ID
     * @return 结果
     */
    List<MaterialPojo> getByMaterialId(Long materialId);
}

