package com.senmol.mes.workflow.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.workflow.utils.FlowAsyncUtil;
import com.senmol.mes.workflow.entity.FlowRecord;
import com.senmol.mes.workflow.entity.FlowRecordUser;
import com.senmol.mes.workflow.entity.FlowUser;
import com.senmol.mes.workflow.mapper.FlowRecordUserMapper;
import com.senmol.mes.workflow.service.FlowRecordService;
import com.senmol.mes.workflow.service.FlowRecordUserService;
import com.senmol.mes.workflow.service.FlowUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 记录审核员(FlowRecordUser)表服务实现类
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
@Service("flowRecordUserService")
public class FlowRecordUserServiceImpl extends ServiceImpl<FlowRecordUserMapper, FlowRecordUser> implements FlowRecordUserService {

    @Resource
    private FlowRecordService flowRecordService;
    @Resource
    private FlowUserService flowUserService;
    @Resource
    private FlowRecordUserServiceImpl flowRecordUserService;
    @Lazy
    @Resource
    private FlowAsyncUtil flowAsyncUtil;

    @Override
    public SaResult updateFlowRecordUser(FlowRecordUser flowRecordUser) {
        long userId = StpUtil.getLoginIdAsLong();
        flowRecordUser.setUserId(userId);

        Long pid = flowRecordUser.getPid();
        this.lambdaUpdate()
                .eq(FlowRecordUser::getPid, pid)
                .eq(FlowRecordUser::getUserId, flowRecordUser.getUserId())
                .update(flowRecordUser);

        // 本次审核不通过
        if (flowRecordUser.getResult() == 2) {
            // 整个流程不通过
            this.flowRecordUserService.updaterRecordStatus(2, flowRecordUser);

            this.flowAsyncUtil.dealFlowStatus(2, pid, userId);
        } else {
            // 查询本流程所有审核员
            List<FlowUser> flowUsers = this.flowUserService.getByPid(pid);
            FlowUser flowUser = flowUsers.stream().filter(item -> item.getUserId().equals(userId)).findFirst().get();
            int indexOf = flowUsers.indexOf(flowUser);
            // 如果是最后一个审核员，整个流程修改为通过状态
            if (indexOf == flowUsers.size() - 1) {
                this.flowRecordUserService.updaterRecordStatus(3, flowRecordUser);

                this.flowAsyncUtil.dealFlowStatus(3, pid, userId);
            } else {
                // 如果是第一个审核员，整个流程修改为未完成状态
                this.flowRecordUserService.updaterRecordStatus(1, flowRecordUser);

                // 记录审核原表插入下一个审核员的信息
                FlowUser user = flowUsers.get(indexOf + 1);
                FlowRecordUser fru = new FlowRecordUser();
                fru.setPid(pid);
                fru.setUserId(user.getUserId());
                fru.setSort(user.getSort());
                this.save(fru);
            }
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    public void updaterRecordStatus(Integer status, FlowRecordUser flowRecordUser) {
        this.flowRecordService.lambdaUpdate()
                .set(FlowRecord::getStatus, status)
                .set(FlowRecord::getRemarks, flowRecordUser.getRemarks())
                .eq(FlowRecord::getId, flowRecordUser.getPid())
                .update();

    }
}

