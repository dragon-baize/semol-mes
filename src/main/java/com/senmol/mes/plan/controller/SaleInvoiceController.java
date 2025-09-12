package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.SaleInvoice;
import com.senmol.mes.plan.page.SaleInvoicePage;
import com.senmol.mes.plan.service.SaleInvoiceService;
import com.senmol.mes.plan.vo.SaleInvoiceVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 销售发货单开票(SaleInvoice)表控制层
 *
 * @author makejava
 * @since 2024-05-23 14:28:01
 */
@RestController
@RequestMapping("/plan/saleInvoice")
public class SaleInvoiceController {

    @Resource
    private SaleInvoiceService saleInvoiceService;

    /**
     * 查询数据
     *
     * @param page          分页对象
     * @param saleInvoiceVo 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(SaleInvoicePage page, SaleInvoiceVo saleInvoiceVo) {
        if (ObjUtil.isNotNull(saleInvoiceVo.getId())) {
            return this.saleInvoiceService.selectOne(saleInvoiceVo.getId());
        }

        return this.saleInvoiceService.selectAll(page, saleInvoiceVo);
    }

    /**
     * 新增数据
     *
     * @param saleInvoice 实体对象
     * @return 新增结果
     */
    @Logger("销售发货单开票")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody SaleInvoice saleInvoice) {
        return this.saleInvoiceService.insert(saleInvoice);
    }

}

