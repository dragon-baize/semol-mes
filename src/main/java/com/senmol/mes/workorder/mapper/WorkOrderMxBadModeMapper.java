package com.senmol.mes.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.workorder.entity.WorkOrderMxBadMode;

import java.math.BigDecimal;

/**
 * (WorkOrderMxBadMode)表数据库访问层
 *
 * @author makejava
 * @since 2023-12-26 09:30:36
 */
public interface WorkOrderMxBadModeMapper extends BaseMapper<WorkOrderMxBadMode> {

    BigDecimal getSumQty();
}

