package com.senmol.mes.workflow.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.workflow.entity.FlowRecordUser;

/**
 * 记录审核员(FlowRecordUser)表服务接口
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
public interface FlowRecordUserService extends IService<FlowRecordUser> {
    /**
     * 流程审批
     *
     * @param flowRecordUser 实体对象
     * @return 修改结果
     */
    SaResult updateFlowRecordUser(FlowRecordUser flowRecordUser);
}

