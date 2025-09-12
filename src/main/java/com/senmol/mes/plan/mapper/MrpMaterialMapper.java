package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.MrpMaterial;
import com.senmol.mes.workorder.vo.MaterialPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MRP物料计算结果(PlanMrpMaterial)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-14 13:09:46
 */
public interface MrpMaterialMapper extends BaseMapper<MrpMaterial> {
    /**
     * 查询物料预占用库存
     *
     * @param materialId 物料ID
     * @return 结果
     */
    List<MaterialPojo> getByMaterialId(@Param("materialId") Long materialId);
}

