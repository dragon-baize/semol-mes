package com.senmol.mes.workorder.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.produce.vo.BomMaterialVo;
import com.senmol.mes.workorder.entity.WorkOrderMaterial;
import com.senmol.mes.workorder.mapper.WorkOrderMaterialMapper;
import com.senmol.mes.workorder.service.WorkOrderMaterialService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 任务单物料(WorkOrderMaterial)表服务实现类
 *
 * @author makejava
 * @since 2023-11-07 13:39:58
 */
@Service("workOrderMaterialService")
public class WorkOrderMaterialServiceImpl extends ServiceImpl<WorkOrderMaterialMapper, WorkOrderMaterial> implements WorkOrderMaterialService {

    @Override
    public List<WorkOrderMaterial> getByMxId(Long mxId) {
        return this.baseMapper.getByMxId(mxId);
    }

    @Override
    public List<Dict> getBaseMaterial(Long mxId, Long processId, BigDecimal count) {
        return this.baseMapper.getBaseMaterial(mxId, processId, count);
    }

    @Override
    public int insertBatch(Long mxId, List<BomMaterialVo> entities) {
        return this.baseMapper.insertBatch(mxId, entities);
    }
}

