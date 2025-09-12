package com.senmol.mes.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.service.OutboundService;
import com.senmol.mes.plan.vo.OutboundMxVo;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.mapper.RetrievalMxMapper;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.warehouse.vo.RetrievalMxVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 出库明细(RetrievalMx)表服务实现类
 *
 * @author makejava
 * @since 2023-07-24 16:54:04
 */
@Service("qrCodeService")
public class RetrievalMxServiceImpl extends ServiceImpl<RetrievalMxMapper, RetrievalMxEntity> implements RetrievalMxService {

    @Resource
    private OutboundService outboundService;

    @Override
    public List<RetrievalMxVo> getByRetrievalId(String pickOrder, Long retrievalId, Integer type) {
        Integer sign = 0;
        if (type == 0) {
            OutboundEntity outbound = this.outboundService.lambdaQuery()
                    .eq(OutboundEntity::getCode, pickOrder)
                    .one();

            sign = outbound.getType();
        }

        return this.baseMapper.getByRetrievalId(retrievalId, type, sign);
    }

    @Override
    public List<OutboundMxVo> getByOutBoundCode(String code) {
        return this.baseMapper.getByOutBoundCode(code);
    }

    @Override
    public List<CountVo> getByObCode(String code) {
        return this.baseMapper.getByObCode(code);
    }

    @Override
    public List<CountVo> getByGdCode(String code) {
        return this.baseMapper.getByGdCode(code);
    }

    @Override
    public void insertBatch(List<RetrievalMxEntity> list) {
        this.baseMapper.insertBatch(list);
    }

    @Override
    public void updateBatchByQrCode(List<RetrievalMxEntity> entities) {
        this.baseMapper.updateBatchByQrCode(entities);
    }

}

