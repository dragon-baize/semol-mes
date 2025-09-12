package com.senmol.mes.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.workflow.entity.FlowUser;

import java.util.List;

/**
 * 审批人员(FlowUser)表服务接口
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
public interface FlowUserService extends IService<FlowUser> {
    /**
     * 查询项目审核员
     *
     * @param pid 项目ID
     * @return 审核员列表
     */
    List<FlowUser> getByPid(Long pid);
}

