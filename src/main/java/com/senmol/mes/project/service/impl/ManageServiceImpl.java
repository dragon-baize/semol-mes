package com.senmol.mes.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.project.entity.Manage;
import com.senmol.mes.project.entity.ManageNape;
import com.senmol.mes.project.entity.ManageStaff;
import com.senmol.mes.project.entity.ManageUser;
import com.senmol.mes.project.mapper.ManageMapper;
import com.senmol.mes.project.service.ManageNapeService;
import com.senmol.mes.project.service.ManageService;
import com.senmol.mes.project.service.ManageStaffService;
import com.senmol.mes.project.service.ManageUserService;
import com.senmol.mes.project.vo.ManageInfo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目管理(Manage)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
@Service("manageService")
public class ManageServiceImpl extends ServiceImpl<ManageMapper, Manage> implements ManageService {

    @Resource
    private ManageUserService manageUserService;
    @Resource
    private ManageNapeService manageNapeService;
    @Resource
    private ManageStaffService manageStaffService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SysFromRedis sysFromRedis;

    @Override
    public Page<Manage> selectAll(Page<Manage> page, Manage manage) {
        Page<Manage> managePage = this.page(page, new LambdaQueryWrapper<>(manage).orderByDesc(Manage::getCreateTime));

        // 产品ID、人员ID转换
        List<Manage> records = managePage.getRecords();
        records.forEach(item -> {
            item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser()));

            ProductVo product = this.proFromRedis.getProduct(item.getProductId());
            if (ObjUtil.isNotNull(product)) {
                item.setProductTitle(product.getTitle());
            }
        });

        return managePage;
    }

    @Override
    public Manage selectOne(Long id) {
        Manage manage = this.getById(id);

        // 查询人员结构
        List<ManageStaff> manageStaffs = this.manageStaffService.lambdaQuery().eq(ManageStaff::getManageId, id).list();
        List<Long> staffIds = manageStaffs.stream().map(ManageStaff::getStaffId).collect(Collectors.toList());
        manage.setStaffIds(staffIds);

        // 查询审核人员
        List<ManageUser> manageUsers = this.manageUserService.lambdaQuery().eq(ManageUser::getManageId, id).list();
        List<Long> userIds = manageUsers.stream().map(ManageUser::getUserId).collect(Collectors.toList());
        manage.setUserIds(userIds);

        // 查询阶段项
        List<ManageNape> manageNapes = this.manageNapeService.lambdaQuery().eq(ManageNape::getManageId, id).list();
        List<Long> napeIds = manageNapes.stream().map(ManageNape::getNapeId).collect(Collectors.toList());
        manage.setNapeIds(napeIds);
        return manage;
    }

    @Override
    public Page<Manage> getByUserId(Page<Manage> page, Manage manage) {
        List<Manage> manages = this.baseMapper.getByUserId(page, manage, StpUtil.getLoginIdAsLong());
        // 产品ID、人员ID转换
        manages.forEach(item -> {
            item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser()));
            ProductVo productVo = this.proFromRedis.getProduct(item.getProductId());
            if (ObjUtil.isNotNull(productVo)) {
                item.setProductTitle(productVo.getTitle());
            }
        });

        page.setRecords(manages);
        return page;
    }

    @Override
    public ManageInfo getOneById(Long id) {
        ManageInfo info = this.baseMapper.getOneById(id);
        // 人员ID、产品ID转换
        info.setCreateUserName(this.sysFromRedis.getUser(info.getCreateUser()));
        ProductVo productVo = this.proFromRedis.getProduct(info.getProductId());
        if (ObjUtil.isNotNull(productVo)) {
            info.setProductTitle(productVo.getTitle());
        }

        // 审批信息列表字符串转换
        List<ManageUser> manageUsers = new ArrayList<>();
        String userApprove = info.getUserApprove();
        String[] split = userApprove.split(",");
        for (String string : split) {
            String[] s = string.split("_");
            ManageUser manageUser = new ManageUser();
            manageUser.setUserId(Long.parseLong(s[0]));
            manageUser.setUserName(this.sysFromRedis.getUser(Long.parseLong(s[0])));
            manageUser.setIsApprove(Integer.parseInt(s[1]));
            if (s.length > 2) {
                manageUser.setRemarks(s[2]);
            }
            manageUsers.add(manageUser);
        }

        info.setManageUsers(manageUsers);
        return info;
    }

    @Override
    public SaResult insertManage(Manage manage) {
        boolean b = this.checkTitle(manage);
        if (b) {
            return SaResult.error("项目名称重复");
        }

        List<Long> staffIds = manage.getStaffIds();
        List<Long> userIds = manage.getUserIds();
        List<Long> napeIds = manage.getNapeIds();
        this.save(manage);

        if (ObjectUtil.isNotNull(manage.getId())) {
            // 保存项目人员数据
            if (CollUtil.isNotEmpty(staffIds)) {
                List<ManageStaff> manageStaffs = new ArrayList<>(staffIds.size());
                for (Long staffId : staffIds) {
                    ManageStaff manageStaff = new ManageStaff();
                    manageStaff.setManageId(manage.getId());
                    manageStaff.setStaffId(staffId);
                    manageStaffs.add(manageStaff);
                }

                this.manageStaffService.saveBatch(manageStaffs);
            }

            // 保存项目审核员数据
            if (CollUtil.isNotEmpty(userIds)) {
                List<ManageUser> manageUsers = new ArrayList<>(userIds.size());
                for (Long userId : userIds) {
                    ManageUser manageUser = new ManageUser();
                    manageUser.setManageId(manage.getId());
                    manageUser.setUserId(userId);
                    manageUsers.add(manageUser);
                }

                this.manageUserService.saveBatch(manageUsers);
            }

            // 保存项目阶段项数据
            if (CollUtil.isNotEmpty(napeIds)) {
                List<ManageNape> manageNapes = new ArrayList<>(napeIds.size());
                for (Long napeId : napeIds) {
                    ManageNape manageNape = new ManageNape();
                    manageNape.setManageId(manage.getId());
                    manageNape.setNapeId(napeId);
                    manageNapes.add(manageNape);
                }

                this.manageNapeService.saveBatch(manageNapes);
            }
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateManage(Manage manage) {
        boolean b = this.checkTitle(manage);
        if (b) {
            return SaResult.error("项目名称重复");
        }

        List<Long> staffIds = manage.getStaffIds();
        // 删除项目人员数据
        this.manageStaffService.lambdaUpdate().eq(ManageStaff::getManageId, manage.getId()).remove();
        // 保存项目人员数据
        if (CollUtil.isNotEmpty(staffIds)) {
            List<ManageStaff> manageStaffs = new ArrayList<>(staffIds.size());
            for (Long staffId : staffIds) {
                ManageStaff manageStaff = new ManageStaff();
                manageStaff.setManageId(manage.getId());
                manageStaff.setStaffId(staffId);
                manageStaffs.add(manageStaff);
            }

            this.manageStaffService.saveBatch(manageStaffs);
        }

        List<Long> userIds = manage.getUserIds();
        // 删除项目审核员数据
        this.manageUserService.lambdaUpdate().eq(ManageUser::getManageId, manage.getId()).remove();
        // 保存项目审核员数据
        if (CollUtil.isNotEmpty(userIds)) {
            List<ManageUser> manageUsers = new ArrayList<>(userIds.size());
            for (Long userId : userIds) {
                ManageUser manageUser = new ManageUser();
                manageUser.setManageId(manage.getId());
                manageUser.setUserId(userId);
                manageUsers.add(manageUser);
            }

            this.manageUserService.saveBatch(manageUsers);
        }

        List<Long> napeIds = manage.getNapeIds();
        // 删除项目阶段项数据
        this.manageNapeService.lambdaUpdate().eq(ManageNape::getManageId, manage.getId()).remove();
        // 保存项目阶段项数据
        if (ObjectUtil.isNotNull(manage.getId()) && CollUtil.isNotEmpty(napeIds)) {
            List<ManageNape> manageNapes = new ArrayList<>(napeIds.size());
            for (Long napeId : napeIds) {
                ManageNape manageNape = new ManageNape();
                manageNape.setManageId(manage.getId());
                manageNape.setNapeId(napeId);
                manageNapes.add(manageNape);
            }

            this.manageNapeService.saveBatch(manageNapes);
        }

        this.updateById(manage);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteManage(List<Long> idList) {
        List<Manage> list = this.lambdaQuery().in(Manage::getId, idList).list();
        long count = list.stream().filter(item -> item.getApproveResult() == 1).count();
        if (count > 0L) {
            return SaResult.error("审核通过的数据不允许删除");
        }

        // 删除项目审核员数据
        this.manageUserService.lambdaUpdate().in(ManageUser::getManageId, idList).remove();
        // 删除项目阶段项数据
        this.manageNapeService.lambdaUpdate().in(ManageNape::getManageId, idList).remove();

        this.removeByIds(idList);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private boolean checkTitle(Manage manage) {
        LambdaQueryWrapper<Manage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Manage::getTitle, manage.getTitle());
        wrapper.ne(Manage::getApproveResult, 2);

        Long id = manage.getId();
        if (ObjUtil.isNotNull(id)) {
            wrapper.ne(Manage::getId, id);
        }

        wrapper.last(CheckToolUtil.LIMIT);
        return this.count(wrapper) > 0L;
    }
}

