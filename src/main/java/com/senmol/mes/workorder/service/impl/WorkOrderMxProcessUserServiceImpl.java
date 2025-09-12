package com.senmol.mes.workorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.workorder.mapper.WorkOrderMxProcessUserMapper;
import com.senmol.mes.workorder.entity.WorkOrderMxProcessUser;
import com.senmol.mes.workorder.service.WorkOrderMxProcessUserService;
import org.springframework.stereotype.Service;

/**
 * 开报工人员(WorkOrderMxProcessUser)表服务实现类
 *
 * @author makejava
 * @since 2024-04-15 13:31:29
 */
@Service("workOrderMxProcessUserService")
public class WorkOrderMxProcessUserServiceImpl extends ServiceImpl<WorkOrderMxProcessUserMapper, WorkOrderMxProcessUser> implements WorkOrderMxProcessUserService {

}

