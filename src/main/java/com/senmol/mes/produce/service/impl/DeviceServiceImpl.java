package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.entity.DeviceEntity;
import com.senmol.mes.produce.entity.StationEntity;
import com.senmol.mes.produce.mapper.DeviceMapper;
import com.senmol.mes.produce.service.DeviceService;
import com.senmol.mes.produce.service.StationService;
import com.senmol.mes.produce.utils.ProAsyncUtil;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备管理(Device)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Service("deviceService")
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DeviceEntity> implements DeviceService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Lazy
    @Resource
    private ProAsyncUtil proAsyncUtil;
    @Resource
    private RedisService redisService;
    @Resource
    private StationService stationService;

    @Override
    public List<DeviceEntity> getList(Long productLineId, Long stationId) {
        return this.baseMapper.getList(productLineId, stationId);
    }

    @Override
    public SaResult selectAll(Page<DeviceEntity> page, DeviceEntity device) {
        this.page(page, new QueryWrapper<>(device));
        List<DeviceEntity> records = page.getRecords();
        records.forEach(item -> item.setStatusTitle(this.sysFromRedis.getDictMx(item.getStatus())));
        return SaResult.data(page);
    }

    @Override
    public SaResult insertDevice(DeviceEntity device) {
        long l = CheckToolUtil.checkCodeExist(this, null, device.getCode());
        if (l > 0L) {
            return SaResult.error("设备编号重复");
        }

        // 保存设备数据
        this.save(device);
        // 添加到缓存
        this.proAsyncUtil.dealDevice(device);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateDevice(DeviceEntity device) {
        long l = CheckToolUtil.checkCodeExist(this, device.getId(), device.getCode());
        if (l > 0L) {
            return SaResult.error("设备编号重复");
        }

        // 更新设备数据
        this.updateById(device);
        // 添加到缓存
        this.proAsyncUtil.dealDevice(device);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteDevice(Long id) {
        long count = this.stationService.lambdaQuery()
                .eq(StationEntity::getDeviceId, id)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("设备已绑定工位");
        }

        this.removeById(id);
        // 删除缓存
        this.redisService.del(RedisKeyEnum.PRODUCE_DEVICE.getKey() + id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

