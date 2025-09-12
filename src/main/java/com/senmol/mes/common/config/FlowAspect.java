package com.senmol.mes.common.config;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.workflow.utils.FlowAsyncUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Aspect
@Component
public class FlowAspect {

    @Resource
    private RedisService redisService;
    @Resource
    private FlowAsyncUtil flowAsyncUtil;
    private static final String TABLE_NAME = "生产计划";

    @AfterReturning(value = "@annotation(flowCache)", returning = "saResult")
    public void doAfterReturning(JoinPoint point, FlowCache flowCache, SaResult saResult) {
        if (saResult.getCode().equals(SaResult.CODE_SUCCESS)) {
            Object param = point.getArgs()[0];

            // 未开启流程的跳过
            Object hGet = this.redisService.hGet(RedisKeyEnum.WORKFLOW_TABLE.getKey(), flowCache.table());
            if (ObjectUtil.isNull(hGet)) {
                // 生产计划推工单
                if (TABLE_NAME.equals(flowCache.table())) {
                    this.flowAsyncUtil.dealProduce(flowCache, param);
                }

                return;
            }

            boolean add = flowCache.isAdd();
            // 开启流程
            if (add) {
                this.flowAsyncUtil.openFlow(hGet, flowCache, param);
            } else {
                // 删除流程
                this.flowAsyncUtil.delFlow(param);
            }
        }
    }




}
