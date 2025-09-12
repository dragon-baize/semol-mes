package com.senmol.mes.workorder.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.produce.utils.ProAsyncUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.DeviceVo;
import com.senmol.mes.produce.vo.ProcessVo;
import com.senmol.mes.produce.vo.StationVo;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.utils.WarAsyncUtil;
import com.senmol.mes.workorder.entity.*;
import com.senmol.mes.workorder.mapper.WorkOrderMxProcessMapper;
import com.senmol.mes.workorder.service.*;
import com.senmol.mes.workorder.utils.WorkOrderAsyncUtil;
import com.senmol.mes.workorder.vo.WorkOrderMxProcessVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 工单工序(WorkOrderMxProcess)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 14:29:45
 */
@Service("workOrderMxProcessService")
public class WorkOrderMxProcessServiceImpl extends ServiceImpl<WorkOrderMxProcessMapper, WorkOrderMxProcess> implements WorkOrderMxProcessService {

    @Resource
    private WorkOrderMaterialService workOrderMaterialService;
    @Resource
    private WarAsyncUtil warAsyncUtil;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private ProAsyncUtil proAsyncUtil;
    @Resource
    private WorkOrderAsyncUtil workOrderAsyncUtil;
    @Resource
    private WorkOrderMxProcessUserService workOrderMxProcessUserService;
    @Resource
    private WorkOrderFeedService workOrderFeedService;
    @Resource
    private WorkOrderMxBadModeService workOrderMxBadModeService;

    @Override
    public List<WorkOrderMxProcessVo> getByMxId(Long mxId) {
        List<WorkOrderMxProcess> list = this.lambdaQuery()
                .eq(WorkOrderMxProcess::getMxId, mxId)
                .orderByAsc(WorkOrderMxProcess::getSerialNo)
                .list();

        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>(1);
        }

        Set<Long> ids = list.stream().map(WorkOrderMxProcess::getId).collect(Collectors.toSet());
        List<WorkOrderMxProcessUser> users = this.workOrderMxProcessUserService.lambdaQuery()
                .in(WorkOrderMxProcessUser::getMxProcessId, ids)
                .list();

        StringBuilder opUser = new StringBuilder();
        List<WorkOrderMxProcessVo> vos = new ArrayList<>(list.size());
        for (WorkOrderMxProcess mxProcess : list) {
            WorkOrderMxProcessVo vo = Convert.convert(WorkOrderMxProcessVo.class, mxProcess);
            StationVo station = this.proFromRedis.getStation(vo.getStationId());
            if (ObjUtil.isNotNull(station)) {
                vo.setStationCode(station.getCode());
                vo.setStationTitle(station.getTitle());
            }

            DeviceVo device = this.proFromRedis.getDevice(vo.getDeviceId());
            if (ObjUtil.isNotNull(device)) {
                vo.setDeviceCode(device.getCode());
                vo.setDeviceTitle(device.getTitle());
            }

            ProcessVo process = this.proFromRedis.getProcess(vo.getProcessId());
            if (ObjUtil.isNotNull(process)) {
                vo.setProcessTitle(process.getTitle());
            }

            opUser.setLength(0);
            users.stream()
                    .filter(item -> mxProcess.getId().equals(item.getMxProcessId()))
                    .forEach(item -> opUser.append(this.sysFromRedis.getUser(item.getUserId())).append(" "));
            vo.setOpUsers(opUser.toString());

            vos.add(vo);
        }

        return vos;
    }

    @Override
    public long countBeginOrder(Long id) {
        return this.baseMapper.countBeginOrder(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SaResult start(WorkOrderMxProcess process) {
        // 查询该工单已有工序
        List<WorkOrderMxProcess> list = this.lambdaQuery()
                .eq(WorkOrderMxProcess::getMxId, process.getMxId())
                .orderByAsc(WorkOrderMxProcess::getSerialNo)
                .list();

        LocalDateTime now = LocalDateTime.now();
        // 第一次开工写入所有工序
        if (CollUtil.isEmpty(list)) {
            List<Long> processIds = getLongs(process, list);
            this.saveBatch(list);

            if (!processIds.contains(process.getProcessId())) {
                return SaResult.ok("工序不存在");
            }
        }

        String result = this.getResult(process, list);
        if (result != null) {
            return SaResult.error(result);
        }

        list.stream()
                .filter(item -> item.getProcessId().equals(process.getProcessId()))
                .findFirst()
                .ifPresent(item -> process.setId(item.getId()));

        boolean b = this.lambdaUpdate()
                .set(WorkOrderMxProcess::getBeginTime, now)
                .set(WorkOrderMxProcess::getStatus, 1)
                .eq(WorkOrderMxProcess::getId, process.getId())
                .update();

        if (b) {
            // 保存开工人员
            Set<Long> userIds = process.getUserIds();
            List<WorkOrderMxProcessUser> users = new ArrayList<>(userIds.size());
            for (Long userId : userIds) {
                WorkOrderMxProcessUser mxProcessUser = new WorkOrderMxProcessUser();
                mxProcessUser.setMxProcessId(process.getId());
                mxProcessUser.setUserId(userId);
                users.add(mxProcessUser);
            }

            this.workOrderMxProcessUserService.saveBatch(users);

            // 第一次开工
            if (list.get(0).getStatus() == 0) {
                this.workOrderAsyncUtil.updateOrderPlan(process.getMxId()).join();
            }
        }

        return SaResult.data(now);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SaResult cancel(Long mxId, Long stationId, Long processId) {
        List<WorkOrderMxProcess> list = this.lambdaQuery()
                .eq(WorkOrderMxProcess::getMxId, mxId)
                .list();

        WorkOrderMxProcess mxProcess = list.stream()
                .filter(item -> item.getProcessId().equals(processId))
                .findFirst()
                .orElse(null);

        if (mxProcess == null) {
            return SaResult.error("工序不存在");
        }

        Long id = mxProcess.getId();
        this.lambdaUpdate()
                .set(WorkOrderMxProcess::getStatus, 0)
                .eq(WorkOrderMxProcess::getId, id)
                .update();

        this.workOrderMxProcessUserService.lambdaUpdate()
                .eq(WorkOrderMxProcessUser::getMxProcessId, id)
                .remove();

        this.workOrderFeedService.lambdaUpdate()
                .eq(WorkOrderFeedEntity::getMxId, mxId)
                .eq(WorkOrderFeedEntity::getStationId, stationId)
                .remove();

        if (list.indexOf(mxProcess) == 0) {
            this.workOrderAsyncUtil.resetPlan(mxId);
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SaResult end(WorkOrderMxProcess process) {
        boolean flag = true;
        List<WorkOrderMxProcess> list = this.lambdaQuery()
                .eq(WorkOrderMxProcess::getMxId, process.getMxId())
                .orderByAsc(WorkOrderMxProcess::getSerialNo)
                .list();

        WorkOrderMxProcess mxProcess = list.stream()
                .filter(item -> item.getProcessId().equals(process.getProcessId()))
                .findFirst()
                .orElse(null);
        if (ObjectUtil.isNull(mxProcess)) {
            return SaResult.error("工序不存在");
        }

        String checkUsers = this.checkUsers(mxProcess.getId(), process.getUserIds());
        if (checkUsers != null) {
            return SaResult.error(checkUsers);
        }

        // 是否已报工过
        if (mxProcess.getStatus() == 2) {
            flag = false;
        }

        // 获取工序节点
        int indexOf = list.indexOf(mxProcess);
        // 校验前节点是否报工
        if (indexOf > 0) {
            Integer status = list.get(indexOf - 1).getStatus();
            if (status == 0) {
                return SaResult.error("存在未报工前置工序");
            }
        }

        BigDecimal defective = new BigDecimal(0);
        List<WorkOrderMxBadMode> badModes = process.getBadModes();
        long createUser = StpUtil.getLoginIdAsLong();
        if (CollUtil.isNotEmpty(badModes)) {
            for (WorkOrderMxBadMode badMode : badModes) {
                badMode.setMxId(process.getMxId());
                badMode.setProcessId(process.getProcessId());
                badMode.setCreateTime(LocalDateTime.now());
                badMode.setCreateUser(createUser);
                defective = defective.add(badMode.getQty());
            }

            this.workOrderMxBadModeService.lambdaUpdate()
                    .eq(WorkOrderMxBadMode::getMxId, process.getMxId())
                    .eq(WorkOrderMxBadMode::getProcessId, process.getProcessId())
                    .remove();

            this.workOrderMxBadModeService.saveBatch(badModes);
        }

        // 良率计算
        BigDecimal nonDefective = process.getNonDefective().add(process.getRework());
        BigDecimal count = defective.add(nonDefective);
        BigDecimal divide = nonDefective.divide(count, 4, RoundingMode.HALF_UP);
        process.setDefective(defective);
        process.setYield(divide.multiply(new BigDecimal(100)));
        process.setEndTime(LocalDateTime.now());
        process.setStatus(2);

        this.lambdaUpdate()
                .eq(WorkOrderMxProcess::getMxId, process.getMxId())
                .eq(WorkOrderMxProcess::getProcessId, process.getProcessId())
                .update(process);

        // 修改出库明细使用量
        if (flag) {
            CompletableFuture<Future<Void>> updateWorkOrderMx =
                    this.workOrderAsyncUtil.updateWorkOrderMx(indexOf, list.size(), process).exceptionally(e -> {
                        e.printStackTrace();
                        throw new BusinessException("更新工单数据失败");
                    });

            process.setStationId(mxProcess.getStationId());
            BigDecimal add = process.getNonDefective().add(process.getDefective());
            CompletableFuture<Void> changeRetrievalMx =
                    this.warAsyncUtil.changeRetrievalMx(add, process).exceptionally(e -> {
                        e.printStackTrace();
                        throw new BusinessException("修改出库明细失败");
                    });

            CompletableFuture<Void> changeLine =
                    this.proAsyncUtil.changeLine(mxProcess.getMxId(), indexOf, list, nonDefective).exceptionally(e -> {
                        e.printStackTrace();
                        throw new BusinessException("产线完成率计算失败");
                    });

            CompletableFuture.allOf(updateWorkOrderMx, changeRetrievalMx, changeLine).join();
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    private List<Long> getLongs(WorkOrderMxProcess process, List<WorkOrderMxProcess> list) {
        List<WorkOrderMaterial> materials = this.workOrderMaterialService.getByMxId(process.getMxId());
        List<Long> processIds = new ArrayList<>(materials.size());
        for (WorkOrderMaterial material : materials) {
            WorkOrderMxProcess mx = new WorkOrderMxProcess();
            mx.setMxId(process.getMxId());
            mx.setStationId(material.getStationId());
            mx.setDeviceId(material.getDeviceId());
            mx.setProcessId(material.getProcessId());
            mx.setSerialNo(material.getSerialNo());
            mx.setStatus(0);
            list.add(mx);
            processIds.add(material.getProcessId());
        }

        return processIds;
    }

    private String getResult(WorkOrderMxProcess process, List<WorkOrderMxProcess> list) {
        WorkOrderMxProcess mxProcess = list.stream()
                .filter(item -> item.getProcessId().equals(process.getProcessId()))
                .findFirst()
                .orElse(null);

        if (ObjectUtil.isNull(mxProcess)) {
            return "工序不存在";
        }

        if (mxProcess.getStatus() == 1) {
            return "工序已开工";
        }

        if (mxProcess.getStatus() == 2) {
            return "工序已报工";
        }

        // 获取工序节点
        int indexOf = list.indexOf(mxProcess);
        // 校验前节点是否开工
        if (indexOf > 0) {
            Integer status = list.get(indexOf - 1).getStatus();
            if (status == 0) {
                return "存在未开工前置工序";
            }
        }

        return null;
    }

    private String checkUsers(Long mxProcessId, Set<Long> userIds) {
        Set<Long> existUserIds = this.workOrderMxProcessUserService.lambdaQuery()
                .eq(WorkOrderMxProcessUser::getMxProcessId, mxProcessId)
                .list()
                .stream()
                .map(WorkOrderMxProcessUser::getUserId)
                .collect(Collectors.toSet());

        if (!existUserIds.equals(userIds)) {
            return "开工与报工人员信息不匹配";
        }

        return null;
    }

}

