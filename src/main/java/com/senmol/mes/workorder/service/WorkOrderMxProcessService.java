package com.senmol.mes.workorder.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.workorder.entity.WorkOrderMxProcess;
import com.senmol.mes.workorder.vo.WorkOrderMxProcessVo;

import java.util.List;

/**
 * 工单工序(WorkOrderMxProcess)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 14:29:45
 */
public interface WorkOrderMxProcessService extends IService<WorkOrderMxProcess> {

    /**
     * 查询工单明细
     *
     * @param mxId 工单ID
     * @return 工单明细
     */
    List<WorkOrderMxProcessVo> getByMxId(Long mxId);

    /**
     * 查询是否存在已开工的任务单
     *
     * @param id 生产计划ID
     * @return 结果
     */
    long countBeginOrder(Long id);

    /**
     * 开工
     *
     * @param process 实体
     * @return 结果
     */
    SaResult start(WorkOrderMxProcess process);

    /**
     * 取消开工
     */
    SaResult cancel(Long mxId, Long stationId, Long processId);

    /**
     * 报工
     *
     * @param process 实体
     * @return 结果
     */
    SaResult end(WorkOrderMxProcess process);

}

