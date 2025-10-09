package com.senmol.mes.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.produce.vo.BomMaterialVo;
import com.senmol.mes.workorder.entity.WorkOrderMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务单物料(WorkOrderMaterial)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-07 13:39:58
 */
public interface WorkOrderMaterialMapper extends BaseMapper<WorkOrderMaterial> {

    /**
     * 查询工单工位工序
     *
     * @param mxId 工单ID
     * @return 结果
     */
    List<WorkOrderMaterial> getByMxId(@Param("mxId") Long mxId);

    /**
     * 获取工单对应物料及其基础使用量
     *
     * @param mxId      工单ID
     * @param processId 工序ID
     * @return 结果
     */
    List<CountVo> getBaseMaterial(@Param("mxId") Long mxId, @Param("processId") Long processId);

    /**
     * 批量保存
     *
     * @param mxId     工单ID
     * @param entities 物料清单数据
     * @return 受影响行数
     */
    int insertBatch(@Param("mxId") Long mxId, @Param("entities") List<BomMaterialVo> entities);

}

