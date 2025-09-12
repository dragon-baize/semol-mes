package com.senmol.mes.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.workorder.entity.WorkOrderMxProcess;
import org.apache.ibatis.annotations.Param;

/**
 * 工单工序(WorkOrderMxProcess)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 14:29:45
 */
public interface WorkOrderMxProcessMapper extends BaseMapper<WorkOrderMxProcess> {

    /**
     * 查询是否存在已开工的任务单
     *
     * @param id 生产计划ID
     * @return 结果
     */
    long countBeginOrder(@Param("id") Long id);

}

