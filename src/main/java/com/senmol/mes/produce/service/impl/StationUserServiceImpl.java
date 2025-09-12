package com.senmol.mes.produce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.entity.StationUserEntity;
import com.senmol.mes.produce.mapper.StationUserMapper;
import com.senmol.mes.produce.service.StationUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工位-人员(StationUser)表服务实现类
 *
 * @author makejava
 * @since 2023-01-31 10:18:13
 */
@Service("stationUserService")
public class StationUserServiceImpl extends ServiceImpl<StationUserMapper, StationUserEntity> implements StationUserService {

    @Override
    public List<ProcessEntity> getByUserId(Long userId) {
        return this.baseMapper.getByUserId(userId);
    }

}

