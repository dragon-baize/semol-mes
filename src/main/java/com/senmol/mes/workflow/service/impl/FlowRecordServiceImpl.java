package com.senmol.mes.workflow.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.workflow.entity.Flow;
import com.senmol.mes.workflow.entity.FlowRecord;
import com.senmol.mes.workflow.entity.FlowRecordUser;
import com.senmol.mes.workflow.entity.FlowUser;
import com.senmol.mes.workflow.mapper.FlowRecordMapper;
import com.senmol.mes.workflow.service.FlowRecordService;
import com.senmol.mes.workflow.service.FlowRecordUserService;
import com.senmol.mes.workflow.service.FlowService;
import com.senmol.mes.workflow.service.FlowUserService;
import com.senmol.mes.workflow.vo.FlowRecordInfo;
import com.senmol.mes.workflow.vo.FlowRecordVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 审核记录(FlowRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
@Service("flowRecordService")
public class FlowRecordServiceImpl extends ServiceImpl<FlowRecordMapper, FlowRecord> implements FlowRecordService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private FlowService flowService;
    @Resource
    private FlowUserService flowUserService;
    @Resource
    private FlowRecordUserService flowRecordUserService;

    @Override
    public FlowRecordInfo selectOne(Long id) {
        FlowRecordInfo info = this.baseMapper.getMxById(id);
        if (BeanUtil.isEmpty(info)) {
            return null;
        }

        // 字典ID转换
        info.setCreateUserName(this.sysFromRedis.getUser(info.getCreateUser()));

        // 审批信息转换
        List<FlowRecordUser> recordUsers = new ArrayList<>();
        String userApprove = info.getUserApprove();
        if (StrUtil.isBlank(userApprove)) {
            return info;
        }

        String[] split = userApprove.split(",");
        for (String string : split) {
            String[] s = string.split("_");
            FlowRecordUser recordUser = new FlowRecordUser();
            recordUser.setUserId(Long.parseLong(s[0]));
            recordUser.setUserName(this.sysFromRedis.getUser(Long.parseLong(s[0])));
            recordUser.setResult(Integer.parseInt(s[1]));
            if (s.length > 2) {
                recordUser.setRemarks(s[2]);
            }
            recordUsers.add(recordUser);
        }

        info.setRecordUsers(recordUsers);
        return info;
    }

    @Override
    public Page<FlowRecordVo> selectAll(Page<FlowRecordVo> page, FlowRecord flowRecord) {
        List<FlowRecordVo> voList = this.baseMapper.selectAll(page, flowRecord, StpUtil.getLoginIdAsLong());
        // 人员ID、流程ID转换
        voList.forEach(item -> item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser())));
        page.setRecords(voList);
        return page;
    }

    @Override
    public void insertFlowRecord(FlowRecord flowRecord) {
        Flow flow = this.flowService.lambdaQuery()
                .eq(Flow::getTitle, flowRecord.getTitle())
                .eq(Flow::getStatus, 1)
                .one();

        flowRecord.setFlowId(flow.getId());
        this.save(flowRecord);

        List<FlowUser> flowUsers = this.flowUserService.lambdaQuery()
                .eq(FlowUser::getFlowId, flowRecord.getFlowId())
                .orderByAsc(FlowUser::getSort)
                .list();

        FlowRecordUser recordUser = new FlowRecordUser();
        recordUser.setPid(flowRecord.getId());
        recordUser.setUserId(flowUsers.get(0).getUserId());
        recordUser.setSort(flowUsers.get(0).getSort());
        this.flowRecordUserService.save(recordUser);

        if (flow.getIsUse() == 0) {
        this.flowService.lambdaUpdate()
                .set(Flow::getIsUse, 1)
                .eq(Flow::getId, flowRecord.getFlowId())
                .update();
        }

        SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public void updateStatus(Object table, Integer status, Object id) {
        this.baseMapper.updateStatus(table, status, id);
    }

    @Override
    public void deleteFlowRecord(Long itemId) {
        FlowRecord flowRecord = this.lambdaQuery().eq(FlowRecord::getItemId, itemId).one();
        if (BeanUtil.isEmpty(flowRecord)) {
            SaResult.ok();
            return;
        }

        // 删除审批人记录
        this.flowRecordUserService.lambdaUpdate()
                .eq(FlowRecordUser::getPid, flowRecord.getId())
                .remove();
        // 删除审批记录
        this.lambdaUpdate().eq(FlowRecord::getItemId, itemId).remove();

        // 校验是否需要恢复流程流转状态
        List<FlowRecord> list = this.lambdaQuery().eq(FlowRecord::getFlowId, flowRecord.getFlowId()).list();
        if (CollUtil.isEmpty(list)) {
            this.flowService.lambdaUpdate()
                    .set(Flow::getIsUse, 0)
                    .eq(Flow::getId, flowRecord.getFlowId())
                    .update();
        }

        SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

