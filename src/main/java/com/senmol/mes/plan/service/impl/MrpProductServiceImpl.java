package com.senmol.mes.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.plan.mapper.MrpProductMapper;
import com.senmol.mes.plan.entity.MrpProduct;
import com.senmol.mes.plan.service.MrpProductService;
import org.springframework.stereotype.Service;

/**
 * MRP产品计算结果(PlanMrpProduct)表服务实现类
 *
 * @author makejava
 * @since 2023-11-14 13:09:46
 */
@Service("planMrpProductService")
public class MrpProductServiceImpl extends ServiceImpl<MrpProductMapper, MrpProduct> implements MrpProductService {

}

