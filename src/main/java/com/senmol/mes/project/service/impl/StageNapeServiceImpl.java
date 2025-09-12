package com.senmol.mes.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.project.entity.NapeUser;
import com.senmol.mes.project.entity.StageNape;
import com.senmol.mes.project.mapper.StageNapeMapper;
import com.senmol.mes.project.service.NapeUserService;
import com.senmol.mes.project.service.StageNapeService;
import com.senmol.mes.project.vo.StageNapeInfo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加项(StageNape)表服务实现类
 *
 * @author makejava
 * @since 2023-03-21 09:27:07
 */
@Service("stageNapeService")
public class StageNapeServiceImpl extends ServiceImpl<StageNapeMapper, StageNape> implements StageNapeService {

    @Resource
    private NapeUserService napeUserService;
    @Resource
    private SysFromRedis sysFromRedis;

    @Override
    public Page<StageNape> selectAll(Page<StageNape> page, StageNape stageNape) {
        List<StageNape> list = this.baseMapper.selectAll(page, stageNape, StpUtil.getLoginIdAsLong());
        list.forEach(item -> item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser())));
        page.setRecords(list);
        return page;
    }

    @Override
    public StageNapeInfo getOneById(Long id) {
        StageNapeInfo info = this.baseMapper.getOneById(id);

        // 人员ID转换
        info.setCreateUserName(this.sysFromRedis.getUser(info.getCreateUser()));

        // 审批信息列表字符串转换
        List<NapeUser> napeUsers = new ArrayList<>();
        String userApprove = info.getUserApprove();
        String[] split = userApprove.split(",");
        for (String string : split) {
            String[] s = string.split("_");
            NapeUser napeUser = new NapeUser();
            napeUser.setUserId(Long.parseLong(s[0]));
            napeUser.setUserName(this.sysFromRedis.getUser(Long.parseLong(s[0])));
            napeUser.setIsApprove(Integer.parseInt(s[1]));
            if (s.length > 2) {
                napeUser.setRemarks(s[2]);
            }
            napeUsers.add(napeUser);
        }

        info.setNapeUsers(napeUsers);
        return info;
    }

    @Override
    public Page<StageNape> getByUserId(Page<StageNape> page, StageNape stageNape) {
        List<StageNape> stageNapes = this.baseMapper.getByUserId(page, stageNape, StpUtil.getLoginIdAsLong());
        stageNapes.forEach(item -> item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser())));
        page.setRecords(stageNapes);
        return page;
    }

    @Override
    public SaResult getByManageId(Long manageId) {
        return SaResult.data(this.baseMapper.getByManageId(manageId));
    }

    @Override
    public SaResult insertStageNape(StageNape stageNape) {
        boolean b = this.checkTitle(stageNape);
        if (b) {
            return SaResult.error("同阶段添加项名称不能重复");
        }

        List<Long> userIds = stageNape.getUserIds();
        if (stageNape.getIsApprove() == 1 && CollUtil.isEmpty(userIds)) {
            return SaResult.error("请选择审核员");
        }

        this.save(stageNape);

        // 保存审核员数据
        if (ObjectUtil.isNotNull(stageNape.getId()) && CollUtil.isNotEmpty(userIds)) {
            List<NapeUser> napeUsers = new ArrayList<>(userIds.size());
            for (Long userId : userIds) {
                NapeUser napeUser = new NapeUser();
                napeUser.setNapeId(stageNape.getId());
                napeUser.setUserId(userId);
                napeUsers.add(napeUser);
            }

            this.napeUserService.saveBatch(napeUsers);
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateStageNape(StageNape stageNape) {
        boolean b = this.checkTitle(stageNape);
        if (b) {
            return SaResult.error("同阶段添加项名称不能重复");
        }

        List<Long> userIds = stageNape.getUserIds();
        if (stageNape.getIsApprove() == 1 && CollUtil.isEmpty(userIds)) {
            return SaResult.error("请选择审核员");
        }

        // 删除审核员数据
        this.napeUserService.lambdaUpdate().eq(NapeUser::getNapeId, stageNape.getId()).remove();
        // 保存审核员数据
        if (CollUtil.isNotEmpty(userIds)) {
            List<NapeUser> napeUsers = new ArrayList<>(userIds.size());
            for (Long userId : userIds) {
                NapeUser napeUser = new NapeUser();
                napeUser.setNapeId(stageNape.getId());
                napeUser.setUserId(userId);
                napeUsers.add(napeUser);
            }

            this.napeUserService.saveBatch(napeUsers);
        }

        this.updateById(stageNape);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteStageNape(Long id) {
        StageNape stageNape = this.getById(id);
        if (BeanUtil.isEmpty(stageNape)) {
            return SaResult.error(ResultEnum.DATA_NOT_EXIST.getMsg());
        }

        if (stageNape.getApproveResult() == 1) {
            return SaResult.error("审核通过的数据不允许删除");
        }

        this.napeUserService.lambdaUpdate().eq(NapeUser::getNapeId, id).remove();
        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    /**
     * 添加项名称不能重复
     */
    private boolean checkTitle(StageNape stageNape) {
        List<StageNape> list = this.list();

        long count;
        if (ObjectUtil.isNotNull(stageNape.getId())) {
            count = list.stream()
                    .filter(item ->
                            !item.getId().equals(stageNape.getId()) &&
                                    item.getTitle().equals(stageNape.getTitle()) &&
                                            item.getBelong().equals(stageNape.getBelong())
                    ).count();
        } else {
            count = list.stream()
                    .filter(item -> item.getTitle().equals(stageNape.getTitle()) &&
                            item.getBelong().equals(stageNape.getBelong()))
                    .count();
        }

        return count > 0L;
    }
}

