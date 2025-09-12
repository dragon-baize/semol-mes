package com.senmol.mes.produce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.produce.entity.StationBadModeEntity;
import com.senmol.mes.produce.mapper.StationBadModeMapper;
import com.senmol.mes.produce.service.StationBadModeService;
import org.springframework.stereotype.Service;

/**
 * 工位-不良模式(StationBadMode)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
@Service("stationBadModeService")
public class StationBadModeServiceImpl extends ServiceImpl<StationBadModeMapper, StationBadModeEntity> implements StationBadModeService {

}

