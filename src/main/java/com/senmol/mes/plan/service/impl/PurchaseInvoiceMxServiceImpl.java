package com.senmol.mes.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.plan.mapper.PurchaseInvoiceMxMapper;
import com.senmol.mes.plan.entity.PurchaseInvoiceMx;
import com.senmol.mes.plan.service.PurchaseInvoiceMxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (PurchaseInvoiceMx)表服务实现类
 *
 * @author dragon-xiaobai
 * @since 2025-09-01 17:06:05
 */
@Service("purchaseInvoiceMxService")
public class PurchaseInvoiceMxServiceImpl extends ServiceImpl<PurchaseInvoiceMxMapper, PurchaseInvoiceMx> implements PurchaseInvoiceMxService {

    @Override
    public void updateBatch(Long pid, List<PurchaseInvoiceMx> list) {
        this.baseMapper.updateBatch(pid, list);
    }

}

