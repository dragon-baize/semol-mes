package com.senmol.mes.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.workflow.mapper.FlowUserMapper;
import com.senmol.mes.workflow.entity.FlowUser;
import com.senmol.mes.workflow.service.FlowUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批人员(FlowUser)表服务实现类
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
@Service("flowUserService")
public class FlowUserServiceImpl extends ServiceImpl<FlowUserMapper, FlowUser> implements FlowUserService {

    @Override
    public List<FlowUser> getByPid(Long pid) {
        return this.baseMapper.getByPid(pid);
    }
}

