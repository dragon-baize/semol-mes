package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.produce.entity.DeviceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备管理(Device)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface DeviceMapper extends BaseMapper<DeviceEntity> {

    /**
     * 查询所有数据
     *
     * @param productLineId 产线ID
     * @param stationId     工位ID
     * @return 所有数据
     */
    List<DeviceEntity> getList(@Param("productLineId") Long productLineId, @Param("stationId") Long stationId);

}

