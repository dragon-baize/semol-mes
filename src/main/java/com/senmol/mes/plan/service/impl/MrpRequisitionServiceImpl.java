package com.senmol.mes.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.plan.mapper.MrpRequisitionMapper;
import com.senmol.mes.plan.entity.MrpRequisitionEntity;
import com.senmol.mes.plan.service.MrpRequisitionService;
import org.springframework.stereotype.Service;

/**
 * MRP-请购单(MrpRequistion)表服务实现类
 *
 * @author makejava
 * @since 2023-07-15 11:20:53
 */
@Service("mrpRequistionService")
public class MrpRequisitionServiceImpl extends ServiceImpl<MrpRequisitionMapper, MrpRequisitionEntity> implements MrpRequisitionService {

}

