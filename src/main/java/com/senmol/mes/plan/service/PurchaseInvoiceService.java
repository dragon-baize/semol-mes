package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.PurchaseInvoice;
import com.senmol.mes.plan.entity.PurchaseInvoiceMx;
import com.senmol.mes.plan.page.PurchaseInvoicePage;
import com.senmol.mes.plan.vo.PurchaseInvoiceVo;

import java.util.List;

/**
 * 采购单开票(PurchaseInvoice)表服务接口
 *
 * @author makejava
 * @since 2024-05-23 16:15:09
 */
public interface PurchaseInvoiceService extends IService<PurchaseInvoice> {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    SaResult selectMx(Long id);

    /**
     * 查询数据
     *
     * @param page              分页对象
     * @param purchaseInvoiceVo 查询实体
     * @return 所有数据
     */
    SaResult selectAll(PurchaseInvoicePage page, PurchaseInvoiceVo purchaseInvoiceVo);

    /**
     * 新增数据
     *
     * @param purchaseInvoice 实体对象
     * @return 新增结果
     */
    SaResult insert(PurchaseInvoice purchaseInvoice);

    /**
     * 修改数据
     *
     * @param list 实体对象列表
     * @return 修改结果
     */
    SaResult modifyMx(List<PurchaseInvoiceMx> list);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult delById(Long id);

}

