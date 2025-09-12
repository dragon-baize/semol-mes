package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.DeviceEntity;

import java.util.List;

/**
 * 设备管理(Device)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface DeviceService extends IService<DeviceEntity> {

    /**
     * 查询所有数据
     *
     * @param stationId     工位ID
     * @param productLineId 产线ID
     * @return 所有数据
     */
    List<DeviceEntity> getList(Long productLineId, Long stationId);

    /**
     * 分页查询所有数据
     *
     * @param page   分页对象
     * @param device 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<DeviceEntity> page, DeviceEntity device);

    /**
     * 新增数据
     *
     * @param device 实体对象
     * @return 新增结果
     */
    SaResult insertDevice(DeviceEntity device);

    /**
     * 修改数据
     *
     * @param device 实体对象
     * @return 修改结果
     */
    SaResult updateDevice(DeviceEntity device);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteDevice(Long id);

}

