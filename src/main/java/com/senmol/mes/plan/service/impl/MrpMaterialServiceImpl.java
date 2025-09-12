package com.senmol.mes.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.plan.mapper.MrpMaterialMapper;
import com.senmol.mes.plan.entity.MrpMaterial;
import com.senmol.mes.plan.service.MrpMaterialService;
import com.senmol.mes.workorder.vo.MaterialPojo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MRP物料计算结果(PlanMrpMaterial)表服务实现类
 *
 * @author makejava
 * @since 2023-11-14 13:09:46
 */
@Service("planMrpMaterialService")
public class MrpMaterialServiceImpl extends ServiceImpl<MrpMaterialMapper, MrpMaterial> implements MrpMaterialService {

    @Override
    public List<MaterialPojo> getByMaterialId(Long materialId) {
        return this.baseMapper.getByMaterialId(materialId);
    }
}

