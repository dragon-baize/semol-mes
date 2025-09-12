package com.senmol.mes.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.workorder.entity.WorkOrderMxEntity;
import com.senmol.mes.workorder.vo.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 工单明细(WorkOrderMx)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-20 11:03:10
 */
public interface WorkOrderMxMapper extends BaseMapper<WorkOrderMxEntity> {
    /**
     * 根据计划ID获取工单明细
     *
     * @param planId 计划ID
     * @return 工单明细列表
     */
    List<WorkOrderMxEntity> getByPlanId(@Param("planId") Long planId);

    /**
     * 查询产线工单
     *
     * @param productLineId 产线ID
     * @return 工单列表
     */
    List<WorkOrderMxDto> getLineOrder(@Param("productLineId") Long productLineId, @Param("planId") Long planId);

    /**
     * 产线查工单
     *
     * @param code 工单单号
     * @return 工位信息
     */
    StationInfo getByMxCode(@Param("code") String code);

    /**
     * 工位物料
     *
     * @param code      工单编号
     * @param stationId 工位ID
     * @return 物料数据
     */
    List<MaterialPojo> getMaterials(@Param("code") String code, @Param("stationId") Long stationId);

    /**
     * 正向追溯
     *
     * @param code 工单编号
     * @return 结果
     */
    List<OrderMaterialInfo> retrospect(@Param("code") String code);

    /**
     * 月周度统计
     *
     * @param wom           月周
     * @param productLineId 产线ID
     * @return 结果
     */
    List<StatInfo> getStatByWeek(@Param("wom") Integer wom,
                                 @Param("productLineId") Long productLineId);

    /**
     * 工单已入库数量
     *
     * @return 入库数量
     */
    BigDecimal getStorageQty();

    /**
     * 良率统计
     *
     * @param ids 任务单ID列表
     * @return 结果
     */
    List<StatInfo> getStatYield(@Param("wom") Integer wom,
                                @Param("ids") List<String> ids);

    /**
     * 查询工单工序
     *
     * @param mxCode 工单编号
     * @return 工序列表
     */
    List<ProcessEntity> getProcesses(@Param("mxCode") String mxCode);

    /**
     * 工单ID查产线信息
     *
     * @param mxId 工单ID
     * @return 产线信息
     */
    ProductLineEntity getLineInfo(@Param("mxId") Long mxId);

    List<ProductLineInfo> statOrderMx(@Param("productLineId") Long productLineId);

    /**
     * 获取产线下生产中的工单
     *
     * @param productLineId 产线ID
     * @return 生产中的工单
     */
    List<ProductPojo> getByLineId(@Param("productLineId") Long productLineId);

    /**
     * 统计实际数量
     *
     * @return 实体对象
     */
    BigDecimal getSumQty();

    /**
     * 查询计划对应工单编号
     *
     * @param planIds 计划ID
     * @return 工单编号
     */
    Set<String> getByPlanIds(@Param("planIds") List<Long> planIds);

}

