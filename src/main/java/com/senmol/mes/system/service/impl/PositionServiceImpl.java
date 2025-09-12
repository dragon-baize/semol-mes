package com.senmol.mes.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.system.entity.PositionEntity;
import com.senmol.mes.system.mapper.PositionMapper;
import com.senmol.mes.system.service.PositionService;
import org.springframework.stereotype.Service;

/**
 * 职位(Position)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 13:37:41
 */
@Service("positionService")
public class PositionServiceImpl extends ServiceImpl<PositionMapper, PositionEntity> implements PositionService {

}

