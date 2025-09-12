package com.senmol.mes.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.project.entity.NapeUser;
import com.senmol.mes.project.entity.StageNape;
import com.senmol.mes.project.mapper.NapeUserMapper;
import com.senmol.mes.project.service.NapeUserService;
import com.senmol.mes.project.service.StageNapeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 添加项审核(NapeUser)表服务实现类
 *
 * @author makejava
 * @since 2023-03-21 17:07:15
 */
@Service("napeUserService")
public class NapeUserServiceImpl extends ServiceImpl<NapeUserMapper, NapeUser> implements NapeUserService {

    @Resource
    private StageNapeService stageNapeService;

    @Override
    public SaResult updateNapeUser(NapeUser napeUser) {
        List<NapeUser> list = this.lambdaQuery().eq(NapeUser::getNapeId, napeUser.getNapeId()).list();
        if (list.size() < 1) {
            return SaResult.error(ResultEnum.DATA_NOT_EXIST.getMsg());
        }

        long count = list.stream().filter(item -> item.getIsApprove() == 2).count();
        if (count > 0L) {
            return SaResult.error("审核不通过的数据无需再审");
        }

        if (ObjectUtil.isNotEmpty(napeUser.getRemarks())) {
            String remarks = napeUser.getRemarks().replace("_", "-");
            napeUser.setRemarks(remarks);
        }
        // 先完成数据记录
        this.lambdaUpdate()
                .eq(NapeUser::getNapeId, napeUser.getNapeId())
                .eq(NapeUser::getUserId, StpUtil.getLoginIdAsLong())
                .update(napeUser);

        // 本次审核为不通过
        if (napeUser.getIsApprove() == 2) {
            // 终止所有审核
            this.stageNapeService.lambdaUpdate()
                    .set(StageNape::getApproveResult, 2)
                    .eq(StageNape::getId, napeUser.getNapeId())
                    .update();
        } else if (napeUser.getIsApprove() == 1) {
            // 校验是否所有审核员都审核完成
            List<NapeUser> napeUsers =
                    list.stream().filter(item -> item.getIsApprove() == 0).collect(Collectors.toList());
            NapeUser one = napeUsers.get(0);
            // 其他人都审完，本次审核人也审核通过，整个流程审核通过
            if (napeUsers.size() == 1 && one.getUserId().equals(napeUser.getUserId())) {
                // 文件通过审核
                this.stageNapeService.lambdaUpdate()
                        .set(StageNape::getApproveResult, 1)
                        .eq(StageNape::getId, napeUser.getNapeId())
                        .update();
            }
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }
}

