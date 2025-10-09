package com.senmol.mes.workorder.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.workorder.entity.WorkOrderMxEntity;
import com.senmol.mes.workorder.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 工单明细(WorkOrderMx)表服务接口
 *
 * @author makejava
 * @since 2023-02-20 11:03:10
 */
public interface WorkOrderMxService extends IService<WorkOrderMxEntity> {

    /**
     * 工单明细查询
     *
     * @param pid 主表ID
     * @return 所有数据
     */
    SaResult getByPid(Long pid);

    /**
     * 根据计划ID获取工单明细
     *
     * @param planId 计划ID
     * @return 工单明细列表
     */
    List<WorkOrderMxEntity> getByPlanId(Long planId);

    /**
     * 工单查产品
     *
     * @param code 工单号
     * @return 产品信息
     */
    SaResult getByCode(String code);

    /**
     * 查询产线工单
     *
     * @param productLine 产线ID
     * @param planId      生产计划ID
     * @return 工单列表
     */
    List<WorkOrderMxDto> getLineOrder(Long productLine, Long planId);

    /**
     * 查询工单信息
     *
     * @param code      工单单号
     * @param stationId 工位ID
     * @return 工位信息
     */
    SaResult getStationInfo(String code, Long stationId);

    /**
     * 查询工位物料
     *
     * @param code      工单编号
     * @param stationId 工位ID
     * @return 结果
     */
    List<MaterialPojo> getMaterials(String code, Long stationId);

    /**
     * 正向追溯
     *
     * @param code 工单编号
     * @return 结果
     */
    List<OrderMaterialInfo> retrospect(String code);

    /**
     * 查询工单工序
     */
    List<ProcessEntity> getProcesses(String mxCode);

    /**
     * 工单ID查产线信息
     *
     * @param mxId 工单ID
     * @return 产线信息
     */
    ProductLineEntity getLineInfo(Long mxId);

    /**
     * 工单已入库数量
     *
     * @return 入库数量
     */
    BigDecimal getStorageQty();

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
    Set<String> getByPlanIds(List<Long> planIds);

    /**
     * 新增数据
     *
     * @param workOrderMx 实体对象
     * @return 新增结果
     */
    SaResult insert(WorkOrderMxEntity workOrderMx);

    /**
     * 批量新增
     *
     * @param workOrderMxs 实体对象
     * @return 新增结果
     */
    SaResult insertBatch(List<WorkOrderMxEntity> workOrderMxs);

    /**
     * 修改数据
     *
     * @param workOrderMx 实体对象
     * @return 修改结果
     */
    SaResult updateWorkOrderMx(WorkOrderMxEntity workOrderMx);

    /**
     * 工单终止
     *
     * @param id 主键
     * @return 修改结果
     */
    SaResult updateCutoff(Long id);

    /**
     * 关单送检
     *
     * @param workOrderMx 实体对象
     * @return 更新结果
     */
    SaResult updateByIdAndSubmit(WorkOrderMxEntity workOrderMx);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteWorkOrderMx(Long id);

    /**
     * 获取产线下生产中的工单
     *
     * @param productLineId 产线ID
     * @return 生产中的工单
     */
    SaResult getByLineId(Long productLineId);

    /**
     * 工作台
     */
    SaResult workbench(Long productLineId);

    /**
     * 工作台统计 周或月
     *
     * @param wom           0-周 1-月
     * @param productLineId 产线ID
     * @return 结果
     */
    List<StatInfo> workbenchStat(Integer wom, Long productLineId);

}

