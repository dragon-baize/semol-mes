package com.senmol.mes.produce.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.produce.entity.*;
import com.senmol.mes.produce.service.*;
import com.senmol.mes.produce.vo.*;
import com.senmol.mes.quality.entity.BadModeEntity;
import com.senmol.mes.quality.vo.BadModeVo;
import com.senmol.mes.workorder.entity.WorkOrderMxProcess;
import com.senmol.mes.workorder.service.WorkOrderMxService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Administrator
 */
@Lazy
@Component
public class ProAsyncUtil {

    @Resource
    private RedisService redisService;
    @Resource
    private StationBadModeService stationBadModeService;
    @Resource
    private StationUserService stationUserService;
    @Resource
    private ProcessService processService;
    @Resource
    private ProductLineService productLineService;
    @Resource
    private WorkOrderMxService workOrderMxService;

    /**
     * 变更产线
     */
    @Async
    public void dealLine(ProductLineEntity productLine) {
        LineVo lineVo = Convert.convert(LineVo.class, productLine);
        this.redisService.set(RedisKeyEnum.PRODUCE_LINE.getKey() + lineVo.getId(), lineVo,
                RedisKeyEnum.PRODUCE_LINE.getTimeout());
    }

    /**
     * 变更产品
     */
    @Async
    public void dealProduct(ProductEntity product) {
        ProductVo productVo = Convert.convert(ProductVo.class, product);
        this.redisService.set(RedisKeyEnum.PRODUCE_PRODUCT.getKey() + productVo.getId(), productVo,
                RedisKeyEnum.PRODUCE_PRODUCT.getTimeout());
    }

    /**
     * 变更物料
     */
    @Async
    public void dealMaterial(MaterialEntity material) {
        MaterialVo materialVo = Convert.convert(MaterialVo.class, material);
        this.redisService.set(RedisKeyEnum.PRODUCE_MATERIAL.getKey() + materialVo.getId(), materialVo,
                RedisKeyEnum.PRODUCE_MATERIAL.getTimeout());
    }

    /**
     * 处理工位
     */
    @Async
    public void dealStation(StationEntity station, Integer iou) {
        Long id = station.getId();

        if (iou > 0) {
            this.stationBadModeService.lambdaUpdate().eq(StationBadModeEntity::getStationId, id).remove();
            this.stationUserService.lambdaUpdate().eq(StationUserEntity::getStationId, id).remove();
        }

        // 获取产线ID、工序ID、设备ID、人员ID列表、不良模式ID列表
        List<Long> badModeIds = station.getBadModeIds();
        List<Long> userIds = station.getUserIds();

        // 保存工位-不良模式数据
        if (CollUtil.isNotEmpty(badModeIds)) {
            List<StationBadModeEntity> entities = new ArrayList<>(badModeIds.size());

            StationBadModeEntity entity;
            for (Long badModeId : badModeIds) {
                entity = new StationBadModeEntity(id, badModeId);
                entities.add(entity);
            }

            this.stationBadModeService.saveBatch(entities);
        }

        // 保存工位-人员数据
        if (CollUtil.isNotEmpty(userIds)) {
            List<StationUserEntity> entities = new ArrayList<>(badModeIds.size());

            StationUserEntity entity;
            for (Long userId : userIds) {
                entity = new StationUserEntity(id, userId);
                entities.add(entity);
            }

            this.stationUserService.saveBatch(entities);
        }

        // 加入缓存
        StationVo stationVo = Convert.convert(StationVo.class, station);
        this.redisService.set(RedisKeyEnum.PRODUCE_STATION.getKey() + id, stationVo,
                RedisKeyEnum.PRODUCE_STATION.getTimeout());
    }

    /**
     * 删除工位
     */
    @Async
    public void delStation(Long id) {
        // 删除工位-人员表数据
        this.stationUserService.lambdaUpdate().eq(StationUserEntity::getStationId, id).remove();
        // 删除工位-不良模式表数据
        this.stationBadModeService.lambdaUpdate().eq(StationBadModeEntity::getStationId, id).remove();
        // 删除缓存
        this.redisService.del(RedisKeyEnum.PRODUCE_STATION.getKey() + id);
    }

    /**
     * 变更设备
     */
    @Async
    public void dealDevice(DeviceEntity device) {
        DeviceVo deviceVo = Convert.convert(DeviceVo.class, device);
        this.redisService.set(RedisKeyEnum.PRODUCE_DEVICE.getKey() + deviceVo.getId(), deviceVo,
                RedisKeyEnum.PRODUCE_DEVICE.getTimeout());
    }

    /**
     * 变更工艺
     */
    @Async
    public void dealWorkmanship(WorkmanshipPojo pojo, Long id, Integer version) {
        List<ProcessEntity> processes = pojo.getProcesses();
        List<Long> processIds = new ArrayList<>(processes.size());
        for (ProcessEntity process : processes) {
            process.setId(null);
            process.setWorkmanshipId(id);
            process.setWmsVersion(version);
            process.setCreateTime(pojo.getCreateTime());
            process.setCreateUser(pojo.getCreateUser());
            this.processService.save(process);

            ProcessVo processVo = Convert.convert(ProcessVo.class, process);
            this.redisService.set(RedisKeyEnum.PRODUCE_PROCESS.getKey() + processVo.getId(), processVo,
                    RedisKeyEnum.PRODUCE_PROCESS.getTimeout());

            processIds.add(process.getId());
        }

        WorkmanshipVo workmanshipVo = Convert.convert(WorkmanshipVo.class, pojo);
        workmanshipVo.setStatus(0);
        workmanshipVo.setProcessIds(processIds);
        this.redisService.set(RedisKeyEnum.PRODUCE_WORKMANSHIP.getKey() + id + ":v" + pojo.getVersion(),
                workmanshipVo, RedisKeyEnum.PRODUCE_WORKMANSHIP.getTimeout());
    }

    /**
     * 工序辅助
     */
    @Async
    public void copyWorkmanship(WorkmanshipPojo pojo, Long id) {
        // 复制工序
        List<ProcessEntity> processes = this.processService.lambdaQuery().eq(ProcessEntity::getWorkmanshipId, pojo.getId())
                .eq(ProcessEntity::getWmsVersion, pojo.getVersion()).list();
        List<Long> processIds = new ArrayList<>(processes.size());
        for (ProcessEntity process : processes) {
            process.setId(null);
            process.setWorkmanshipId(id);
            process.setWmsVersion(1);
            process.setCreateTime(pojo.getCreateTime());
            process.setCreateUser(pojo.getCreateUser());
            this.processService.save(process);

            ProcessVo processVo = Convert.convert(ProcessVo.class, process);
            this.redisService.set(RedisKeyEnum.PRODUCE_PROCESS.getKey() + processVo.getId(), processVo,
                    RedisKeyEnum.PRODUCE_PROCESS.getTimeout());

            processIds.add(process.getId());
        }

        WorkmanshipVo workmanshipVo = Convert.convert(WorkmanshipVo.class, pojo);
        workmanshipVo.setStatus(0);
        workmanshipVo.setProcessIds(processIds);
        this.redisService.set(RedisKeyEnum.PRODUCE_WORKMANSHIP.getKey() + id + ":v1", workmanshipVo,
                RedisKeyEnum.PRODUCE_WORKMANSHIP.getTimeout());
    }

    /**
     * 删除工艺
     */
    @Async
    public void delWorkmanship(List<WorkmanshipMx> list, Long id) {
        // 删除工艺-工序表数据
        List<ProcessEntity> processes =
                this.processService.lambdaQuery().eq(ProcessEntity::getWorkmanshipId, id).list();
        for (ProcessEntity process : processes) {
            this.redisService.del(RedisKeyEnum.PRODUCE_PROCESS.getKey() + process.getId());
        }

        this.processService.lambdaUpdate().eq(ProcessEntity::getWorkmanshipId, id).remove();

        for (WorkmanshipMx mx : list) {
            // 删除缓存
            this.redisService.del(RedisKeyEnum.PRODUCE_WORKMANSHIP.getKey() + id + "v" + mx.getWmsVersion());
        }
    }

    /**
     * 变更不良模式
     */
    public void dealBadMode(BadModeEntity badMode) {
        BadModeVo badModeVo = Convert.convert(BadModeVo.class, badMode);
        this.redisService.set(RedisKeyEnum.QUALITY_BAD_MODE.getKey() + badModeVo.getId(), badModeVo,
                RedisKeyEnum.QUALITY_BAD_MODE.getTimeout());
    }

    /**
     * 变更产线完成率
     */
    @Async
    public CompletableFuture<Void> changeLine(Long mxId, int indexOf, List<WorkOrderMxProcess> list, BigDecimal qty) {
        // 获取产线数据
        ProductLineEntity productLine = this.workOrderMxService.getLineInfo(mxId);
        // 总数
        BigDecimal total = productLine.getTotal();
        BigDecimal rate;

        // 当前工序
        BigDecimal current = new BigDecimal(indexOf + 1);
        // 总工序
        BigDecimal all = new BigDecimal(list.size());
        // 新工单报工
        if (indexOf == 0) {
            BigDecimal h = current.divide(all, 4, RoundingMode.HALF_UP);
            BigDecimal multiply = qty.divide(total, 4, RoundingMode.HALF_UP).multiply(h).setScale(4, RoundingMode.HALF_UP);
            rate = productLine.getRate().add(multiply);
        } else {
            // 已存在工单的报工
            rate = productLine.getRate();
            // 上次工序数量
            WorkOrderMxProcess mxProcess = list.get(indexOf - 1);
            BigDecimal lastS = mxProcess.getNonDefective().add(mxProcess.getRework());

            BigDecimal last = new BigDecimal(indexOf).multiply(lastS);
            BigDecimal multiply = total.multiply(all);
            BigDecimal divide = current.multiply(qty).subtract(last).divide(multiply, 4, RoundingMode.HALF_UP);
            rate = rate.add(divide);
        }

        this.productLineService.lambdaUpdate()
                .set(ProductLineEntity::getRate, rate)
                .eq(ProductLineEntity::getId, productLine.getId())
                .update();

        return CompletableFuture.completedFuture(null);
    }

}
