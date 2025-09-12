package com.senmol.mes.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.project.entity.Manage;
import com.senmol.mes.project.entity.ManageUser;
import com.senmol.mes.project.mapper.ManageUserMapper;
import com.senmol.mes.project.service.ManageService;
import com.senmol.mes.project.service.ManageUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目审核(ManageUser)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
@Service("manageUserService")
public class ManageUserServiceImpl extends ServiceImpl<ManageUserMapper, ManageUser> implements ManageUserService {

    @Resource
    private ManageService manageService;

    @Override
    public SaResult updateManageUser(ManageUser manageUser) {
        if (ObjectUtil.isNotNull(manageUser.getRemarks())) {
            String remarks = manageUser.getRemarks().replaceAll("_", "-");
            manageUser.setRemarks(remarks);
        }

        this.lambdaUpdate()
                .eq(ManageUser::getManageId, manageUser.getManageId())
                .eq(ManageUser::getUserId, StpUtil.getLoginIdAsLong())
                .update(manageUser);

        List<ManageUser> list = this.lambdaQuery().eq(ManageUser::getManageId, manageUser.getManageId()).list();

        // 本次审核不通过
        if (manageUser.getIsApprove() == 2) {
            // 项目不通过
            this.manageService.lambdaUpdate()
                    .set(Manage::getApproveResult, 2)
                    .eq(Manage::getId, manageUser.getManageId())
                    .update();
        } else {
            // 是否还有未审核的
            long count = list.stream()
                    .filter(item -> item.getIsApprove() == 0)
                    .count();
            // 没有
            if (count == 0L) {
                // 项目通过
                this.manageService.lambdaUpdate()
                        .set(Manage::getApproveResult, 1)
                        .eq(Manage::getId, manageUser.getManageId())
                        .update();
            }
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }
}

