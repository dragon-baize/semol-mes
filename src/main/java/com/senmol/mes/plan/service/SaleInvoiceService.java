package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.SaleInvoice;
import com.senmol.mes.plan.page.SaleInvoicePage;
import com.senmol.mes.plan.vo.SaleInvoiceVo;

/**
 * 销售发货单开票(SaleInvoice)表服务接口
 *
 * @author makejava
 * @since 2024-05-23 14:28:01
 */
public interface SaleInvoiceService extends IService<SaleInvoice> {

    /**
     * 开票查看
     *
     * @param id 主键
     * @return 结果
     */
    SaResult selectOne(Long id);

    /**
     * 查询数据
     *
     * @param page          分页对象
     * @param saleInvoiceVo 查询实体
     * @return 所有数据
     */
    SaResult selectAll(SaleInvoicePage page, SaleInvoiceVo saleInvoiceVo);

    /**
     * 新增数据
     *
     * @param saleInvoice 实体对象
     * @return 新增结果
     */
    SaResult insert(SaleInvoice saleInvoice);

}

