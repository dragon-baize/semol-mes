package com.senmol.mes.produce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.mapper.ProcessMapper;
import com.senmol.mes.produce.service.ProcessService;
import com.senmol.mes.produce.vo.ProcessVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工序管理(Process)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Service("processService")
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, ProcessEntity> implements ProcessService {

    @Override
    public List<ProcessEntity> getUnboundList(Long workmanshipId, Integer wmsVersion) {
        return this.lambdaQuery().eq(ProcessEntity::getWorkmanshipId, workmanshipId)
                .eq(ProcessEntity::getWmsVersion, wmsVersion).list();
    }

    @Override
    public List<ProcessEntity> getListByWorkmanshipId(Long id, Integer wmsVersion) {
        return this.baseMapper.getListByWorkmanshipId(id, wmsVersion);
    }

    @Override
    public List<ProcessEntity> byProductId(Long productId) {
        return this.baseMapper.byProductId(productId);
    }

    @Override
    public ProcessVo getByIdOrDel(Long id) {
        return this.baseMapper.getByIdOrDel(id);
    }
}

