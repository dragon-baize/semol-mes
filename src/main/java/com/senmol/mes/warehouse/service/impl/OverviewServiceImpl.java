package com.senmol.mes.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.warehouse.mapper.OverviewMapper;
import com.senmol.mes.warehouse.entity.OverviewEntity;
import com.senmol.mes.warehouse.service.OverviewService;
import org.springframework.stereotype.Service;

/**
 * 仓库总览(Overview)表服务实现类
 *
 * @author makejava
 * @since 2023-07-29 10:10:55
 */
@Service("overviewService")
public class OverviewServiceImpl extends ServiceImpl<OverviewMapper, OverviewEntity> implements OverviewService {

}

