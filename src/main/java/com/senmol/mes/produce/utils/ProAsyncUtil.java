package com.senmol.mes.produce.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.produce.entity.*;
import com.senmol.mes.produce.service.ProcessService;
import com.senmol.mes.produce.service.StationBadModeService;
import com.senmol.mes.produce.service.StationUserService;
import com.senmol.mes.produce.vo.*;
import com.senmol.mes.quality.entity.BadModeEntity;
import com.senmol.mes.quality.vo.BadModeVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    @Transactional(rollbackFor = Exception.class)
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

}
