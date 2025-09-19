package com.senmol.mes.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.OutboundMxEntity;
import com.senmol.mes.plan.mapper.OutboundMxMapper;
import com.senmol.mes.plan.service.OutboundMxService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 出库单明细(OutboundMx)表服务实现类
 *
 * @author makejava
 * @since 2023-07-28 18:39:11
 */
@Service("outboundMxService")
public class OutboundMxServiceImpl extends ServiceImpl<OutboundMxMapper, OutboundMxEntity> implements OutboundMxService {

    @Override
    public List<CountVo> getCkQty(Set<String> codes, Set<Long> ids, Integer sign, Integer type) {
        return this.baseMapper.getCkQty(codes, ids, sign, type);
    }

    @Override
    public List<CountVo> getCkQty(Set<String> codes, Long materialId, Integer type) {
        return this.baseMapper.getSckQty(codes, materialId, type);
    }

    @Override
    public List<OutboundMxEntity> getByCode(String code) {
        return this.baseMapper.getByCode(code);
    }

    @Override
    public List<Long> getNotShip(Long outboundId) {
        return this.baseMapper.getNotShip(outboundId);
    }

    @Override
    public List<CountVo> getUnshipQty(List<Long> productIds) {
        return this.baseMapper.getUnshipQty(productIds);
    }

    @Override
    public void modifyBatch(List<CountVo> mxVos) {
        this.baseMapper.modifyBatch(mxVos);
    }

}

