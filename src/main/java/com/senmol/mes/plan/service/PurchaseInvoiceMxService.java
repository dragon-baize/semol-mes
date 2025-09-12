package com.senmol.mes.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.PurchaseInvoiceMx;

import java.util.List;

/**
 * (PurchaseInvoiceMx)表服务接口
 *
 * @author dragon-xiaobai
 * @since 2025-09-01 17:06:05
 */
public interface PurchaseInvoiceMxService extends IService<PurchaseInvoiceMx> {

    /**
     * 批量修改数量、含税价
     *
     * @param pid  父ID
     * @param list 对象列表
     */
    void updateBatch(Long pid, List<PurchaseInvoiceMx> list);

}

