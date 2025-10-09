package com.senmol.mes.workorder.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.produce.service.ProductLineService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.DeviceVo;
import com.senmol.mes.produce.vo.ProcessVo;
import com.senmol.mes.produce.vo.StationVo;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.workorder.entity.*;
import com.senmol.mes.workorder.mapper.WorkOrderMxProcessMapper;
import com.senmol.mes.workorder.service.*;
import com.senmol.mes.workorder.utils.WorkOrderAsyncUtil;
import com.senmol.mes.workorder.vo.WorkOrderMxProcessVo;
import com.senmol.mes.workorder.vo.WorkOrderPojo;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
    private ProFromRedis proFromRedis;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private RedisService redisService;
    @Resource
    private WorkOrderAsyncUtil workOrderAsyncUtil;
    @Resource
    private WorkOrderMxProcessUserService workOrderMxProcessUserService;
    @Resource
    private WorkOrderFeedService workOrderFeedService;
    @Resource
    private WorkOrderMxBadModeService workOrderMxBadModeService;
    @Resource
    private RetrievalMxService retrievalMxService;
    @Resource
    private WorkOrderMxService workOrderMxService;
    @Resource
    private ProductLineService productLineService;
    @Resource
    private ProduceService produceService;
    @Resource
    private ThreadPoolTaskExecutor executor;

    @Override
    public List<WorkOrderMxProcessVo> getByMxId(Long mxId) {
        List<WorkOrderMxProcess> list = this.lambdaQuery().eq(WorkOrderMxProcess::getMxId, mxId)
                .orderByAsc(WorkOrderMxProcess::getSerialNo).list();
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>(1);
        }

        Set<Long> ids = list.stream().map(WorkOrderMxProcess::getId).collect(Collectors.toSet());
        List<WorkOrderMxProcessUser> users = this.workOrderMxProcessUserService.lambdaQuery()
                .in(WorkOrderMxProcessUser::getMxProcessId, ids).list();

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
            users.stream().filter(item -> mxProcess.getId().equals(item.getMxProcessId()))
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
    @Transactional(rollbackFor = Exception.class)
    public SaResult start(WorkOrderMxProcess process) {
        // 查询该工单已有工序
        List<WorkOrderMxProcess> list = this.lambdaQuery().eq(WorkOrderMxProcess::getMxId, process.getMxId())
                .orderByAsc(WorkOrderMxProcess::getSerialNo).list();

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

        list.stream().filter(item -> item.getProcessId().equals(process.getProcessId()))
                .findFirst().ifPresent(item -> process.setId(item.getId()));

        boolean b = this.lambdaUpdate().set(WorkOrderMxProcess::getBeginTime, now)
                .set(WorkOrderMxProcess::getStatus, 1).eq(WorkOrderMxProcess::getId, process.getId()).update();
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
                this.updateOrderPlan(process.getMxId());
            }
        }

        return SaResult.data(now);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult cancel(Long mxId, Long stationId, Long processId) {
        List<WorkOrderMxProcess> list = this.lambdaQuery().eq(WorkOrderMxProcess::getMxId, mxId).list();

        WorkOrderMxProcess mxProcess = list.stream().filter(item -> item.getProcessId().equals(processId))
                .findFirst().orElse(null);
        if (mxProcess == null) {
            return SaResult.error("工序不存在");
        }

        Long id = mxProcess.getId();
        this.lambdaUpdate().set(WorkOrderMxProcess::getStatus, 0).eq(WorkOrderMxProcess::getId, id).update();

        this.workOrderMxProcessUserService.lambdaUpdate().eq(WorkOrderMxProcessUser::getMxProcessId, id).remove();

        this.workOrderFeedService.lambdaUpdate().eq(WorkOrderFeedEntity::getMxId, mxId)
                .eq(WorkOrderFeedEntity::getStationId, stationId).remove();

        this.workOrderMxBadModeService.lambdaUpdate().eq(WorkOrderMxBadMode::getMxId, id)
                .eq(WorkOrderMxBadMode::getProcessId, mxProcess.getProcessId()).remove();

        if (list.indexOf(mxProcess) == 0) {
            this.workOrderAsyncUtil.resetPlan(mxId);
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult end(WorkOrderMxProcess process) {
        List<WorkOrderMxProcess> list = this.lambdaQuery().eq(WorkOrderMxProcess::getMxId, process.getMxId())
                .orderByAsc(WorkOrderMxProcess::getSerialNo).list();

        WorkOrderMxProcess mxProcess = list.stream().filter(item -> item.getProcessId().equals(process.getProcessId()))
                .findFirst().orElse(null);
        if (ObjectUtil.isNull(mxProcess)) {
            return SaResult.error("工序不存在");
        }

        String checkUsers = this.checkUsers(mxProcess.getId(), process.getUserIds());
        if (checkUsers != null) {
            return SaResult.error(checkUsers);
        }

        int indexOf = list.indexOf(mxProcess);
        if (indexOf > 0) {
            Integer status = list.get(indexOf - 1).getStatus();
            if (status == 0) {
                return SaResult.error("存在未报工前置工序");
            }
        }

        BigDecimal defective = this.saveBadMode(process);
        // 是否已报工过
        boolean flag = mxProcess.getStatus() == 2;
        BigDecimal nonDefective = this.updateMxProcess(process, defective);
        this.updateWorkOrderMx(indexOf, list.size(), process, nonDefective);

        CompletableFuture<List<RetrievalMxEntity>> changeRetrievalMx = CompletableFuture.supplyAsync(() -> {
            process.setStationId(mxProcess.getStationId());
            if (!flag) {
                return this.changeRetrievalMx(process.getNonDefective().add(defective), process);
            }

            return null;
        }, this.executor).exceptionally(e -> {
            log.error("上料使用量计算失败", e);
            throw new BusinessException("上料使用量计算失败");
        });

        CompletableFuture<CountVo> changeLine = CompletableFuture.supplyAsync(() ->
                        this.changeLine(mxProcess.getMxId(), indexOf, list, process, nonDefective, flag),
                this.executor).exceptionally(e -> {
            log.error("产线完成率计算失败", e);
            throw new BusinessException("产线完成率计算失败");
        });

        CompletableFuture.allOf(changeRetrievalMx, changeLine).join();
        try {
            List<RetrievalMxEntity> entities = changeRetrievalMx.join();
            if (CollUtil.isNotEmpty(entities)) {
                this.retrievalMxService.updateBatchByQrCode(entities);
            }

            CountVo countVo = changeLine.join();
            this.productLineService.lambdaUpdate().set(ProductLineEntity::getRate, countVo.getQty())
                    .eq(ProductLineEntity::getId, countVo.getAId()).update();
        } catch (Exception e) {
            log.error("上料使用量、产线完成率保存失败", e);
            throw new BusinessException("上料使用量、产线完成率保存失败");
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 保存工单不良模式
     */
    private BigDecimal saveBadMode(WorkOrderMxProcess process) {
        BigDecimal defective = BigDecimal.ZERO;
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

            this.workOrderMxBadModeService.lambdaUpdate().eq(WorkOrderMxBadMode::getMxId, process.getMxId())
                    .eq(WorkOrderMxBadMode::getProcessId, process.getProcessId()).remove();
            this.workOrderMxBadModeService.saveBatch(badModes);
        }

        return defective;
    }

    /**
     * 更新工单工序
     */
    private BigDecimal updateMxProcess(WorkOrderMxProcess process, BigDecimal defective) {
        // 良率计算
        BigDecimal rework = process.getRework();
        if (ObjUtil.isNull(rework)) {
            process.setRework(BigDecimal.ZERO);
            rework = BigDecimal.ZERO;
        }

        BigDecimal nonDefective = process.getNonDefective().add(rework);
        BigDecimal divide = nonDefective.divide(defective.add(nonDefective), 4, RoundingMode.HALF_UP);
        process.setDefective(defective);
        process.setYield(divide.multiply(BigDecimal.valueOf(100)));
        process.setEndTime(LocalDateTime.now());
        process.setStatus(2);
        this.lambdaUpdate().eq(WorkOrderMxProcess::getMxId, process.getMxId())
                .eq(WorkOrderMxProcess::getProcessId, process.getProcessId()).update(process);
        return nonDefective;
    }

    private void updateOrderPlan(Long mxId) {
        // 更新工单开工时间
        this.workOrderMxService.lambdaUpdate()
                .set(WorkOrderMxEntity::getBeginTime, LocalDateTime.now())
                .eq(WorkOrderMxEntity::getId, mxId)
                .update();

        // 更新计划状态为生产中
        WorkOrderPojo workOrder = this.workOrderAsyncUtil.getPlanId(mxId);
        if (BeanUtil.isNotEmpty(workOrder) && workOrder.getExist() == 0) {
            this.produceService.lambdaUpdate().set(ProduceEntity::getStatus, 1)
                    .eq(ProduceEntity::getId, workOrder.getId()).update();

            workOrder.setExist(1);
            this.redisService.set(RedisKeyEnum.PLAN_PRODUCE_ID.getKey() + mxId, workOrder);
        }
    }

    /**
     * 修改出库明细使用量
     */
    private void updateWorkOrderMx(int indexOf, int size, WorkOrderMxProcess process, BigDecimal nonDefective) {
        // 最后一道工序更新工单数据
        if (indexOf == size - 1) {
            this.workOrderMxService.lambdaUpdate().set(WorkOrderMxEntity::getEndTime, LocalDateTime.now())
                    .set(WorkOrderMxEntity::getFinish, 1)
                    .set(WorkOrderMxEntity::getNonDefective, nonDefective)
                    .set(WorkOrderMxEntity::getDefective, process.getDefective())
                    .set(WorkOrderMxEntity::getYield, process.getYield())
                    .set(WorkOrderMxEntity::getUpdateTime, process.getUpdateTime())
                    .eq(WorkOrderMxEntity::getId, process.getMxId())
                    .update();

            this.redisService.del(RedisKeyEnum.PLAN_PRODUCE_ID.getKey() + process.getMxId());
        } else {
            this.workOrderMxService.lambdaUpdate()
                    .set(WorkOrderMxEntity::getNonDefective, nonDefective)
                    .set(WorkOrderMxEntity::getDefective, process.getDefective())
                    .set(WorkOrderMxEntity::getYield, process.getYield())
                    .set(WorkOrderMxEntity::getUpdateTime, process.getUpdateTime())
                    .eq(WorkOrderMxEntity::getId, process.getMxId())
                    .update();
        }
    }

    /**
     * 变更出库明细表数据
     */
    private List<RetrievalMxEntity> changeRetrievalMx(BigDecimal count, WorkOrderMxProcess process) {
        Long mxId = process.getMxId();
        // 获取工单对应物料及其基础使用量
        List<CountVo> countVos = this.workOrderMaterialService.getBaseMaterial(mxId, process.getProcessId());
        if (CollUtil.isEmpty(countVos) || countVos.get(0) == null) {
            return null;
        }

        List<Long> materialIds = countVos.stream().map(CountVo::getAId).collect(Collectors.toList());
        // 获取该种物料上料记录
        List<WorkOrderFeedEntity> list = this.workOrderFeedService.getByMaterials(mxId, process.getStationId(),
                materialIds);

        List<RetrievalMxEntity> entities = new ArrayList<>();
        for (CountVo vo : countVos) {
            Long materialId = vo.getAId();
            BigDecimal qty = vo.getQty().multiply(count);

            List<WorkOrderFeedEntity> feeds = list.stream().filter(item -> item.getMaterialId().equals(materialId))
                    .sorted(Comparator.comparing(WorkOrderFeedEntity::getCreateTime)).collect(Collectors.toList());
            // 计算物料使用量，不够的部分从其他单子上的物料取：qty = subtract.abs()
            for (WorkOrderFeedEntity feed : feeds) {
                // 扫码量 - 之前的使用量 - 本工序总量
                BigDecimal result = feed.getQty().subtract(feed.getUsedQty());
                BigDecimal subtract = result.subtract(qty);

                RetrievalMxEntity entity = new RetrievalMxEntity();
                entity.setQrCode(feed.getQrCode());
                if (subtract.compareTo(BigDecimal.ZERO) >= 0) {
                    entity.setUsedQty(qty);
                    entities.add(entity);
                    break;
                } else {
                    entity.setUsedQty(result);
                    entities.add(entity);
                    qty = subtract.abs();
                }
            }
        }

        return entities;
    }

    /**
     * 变更产线完成率
     */
    private CountVo changeLine(Long mxId, int indexOf, List<WorkOrderMxProcess> list,
                               WorkOrderMxProcess process, BigDecimal qty, boolean flag) {
        // 获取产线数据
        ProductLineEntity productLine = this.workOrderMxService.getLineInfo(mxId);
        BigDecimal total = productLine.getTotal();
        BigDecimal rate = productLine.getRate();

        BigDecimal current = BigDecimal.valueOf(indexOf + 1);
        BigDecimal all = BigDecimal.valueOf(list.size());
        BigDecimal allTotal = all.multiply(total);
        // 新工单报工
        if (indexOf == 0) {
            if (flag) {
                WorkOrderMxProcess mxProcess = list.get(0);
                BigDecimal divide = mxProcess.getNonDefective().add(mxProcess.getRework()).divide(allTotal, 4,
                        RoundingMode.HALF_UP);
                rate = rate.subtract(divide);
            }

            BigDecimal divide = current.multiply(qty).divide(allTotal, 4, RoundingMode.HALF_UP);
            rate = rate.add(divide);
        } else {// 已存在工单的报工
            WorkOrderMxProcess mxProcess = list.get(indexOf - 1);
            BigDecimal multiply =
                    mxProcess.getNonDefective().add(mxProcess.getRework()).multiply(BigDecimal.valueOf(indexOf));
            if (flag) {
                BigDecimal divide = process.getNonDefective().add(process.getRework()).multiply(current)
                        .subtract(multiply).divide(allTotal, 4, RoundingMode.HALF_UP);
                rate = rate.subtract(divide);
            }

            BigDecimal divide = current.multiply(qty).subtract(multiply).divide(allTotal, 4, RoundingMode.HALF_UP);
            rate = rate.add(divide);
        }

        return new CountVo(productLine.getId(), null, null, rate);
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

    /**
     * 开工与报工要求是同一个人
     */
    private String checkUsers(Long mxProcessId, Set<Long> userIds) {
        Set<Long> existUserIds = this.workOrderMxProcessUserService.lambdaQuery()
                .eq(WorkOrderMxProcessUser::getMxProcessId, mxProcessId).list()
                .stream().map(WorkOrderMxProcessUser::getUserId).collect(Collectors.toSet());

        if (!existUserIds.equals(userIds)) {
            return "开工与报工人员信息不匹配";
        }

        return null;
    }

}

