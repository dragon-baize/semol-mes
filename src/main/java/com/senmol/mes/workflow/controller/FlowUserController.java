package com.senmol.mes.workflow.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.workflow.entity.FlowUser;
import com.senmol.mes.workflow.service.FlowUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批人员(FlowUser)表控制层
 *
 * @author makejava
 * @since 2023-03-25 16:24:57
 */
@Validated
@RestController
@RequestMapping("/work/flow/user")
public class FlowUserController {

    @Resource
    private FlowUserService flowUserService;
    @Resource
    private SysFromRedis sysFromRedis;

    /**
     * 查询所有数据
     *
     * @param flowId 流程ID
     * @return 所有数据
     */
    @GetMapping("{flowId}")
    public SaResult selectAll(@PathVariable("flowId") Long flowId) {
        List<FlowUser> list = this.flowUserService.lambdaQuery()
                .eq(FlowUser::getFlowId, flowId)
                .orderByAsc(FlowUser::getSort)
                .list();

        list.forEach(item -> item.setUserName(this.sysFromRedis.getUser(item.getUserId())));
        return SaResult.data(list);
    }

}

