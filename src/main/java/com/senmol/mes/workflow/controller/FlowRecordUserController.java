package com.senmol.mes.workflow.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.workflow.entity.FlowRecordUser;
import com.senmol.mes.workflow.service.FlowRecordUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 记录审核员(FlowRecordUser)表控制层
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
@Validated
@RestController
@RequestMapping("/work/flow/record/user")
public class FlowRecordUserController {

    @Resource
    private FlowRecordUserService flowRecordUserService;

    /**
     * 流程审批
     *
     * @param flowRecordUser 实体对象
     * @return 修改结果
     */
    @Logger("审核")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody FlowRecordUser flowRecordUser) {
        return this.flowRecordUserService.updateFlowRecordUser(flowRecordUser);
    }

}

