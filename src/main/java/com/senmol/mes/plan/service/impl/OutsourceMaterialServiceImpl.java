package com.senmol.mes.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.plan.entity.OutsourceMaterialEntity;
import com.senmol.mes.plan.mapper.OutsourceMaterialMapper;
import com.senmol.mes.plan.service.OutsourceMaterialService;
import org.springframework.stereotype.Service;

/**
 * 委外-物料(OutsourceMaterial)表服务实现类
 *
 * @author makejava
 * @since 2023-03-09 15:59:26
 */
@Service("outsourceMaterialService")
public class OutsourceMaterialServiceImpl extends ServiceImpl<OutsourceMaterialMapper, OutsourceMaterialEntity> implements OutsourceMaterialService {

}

