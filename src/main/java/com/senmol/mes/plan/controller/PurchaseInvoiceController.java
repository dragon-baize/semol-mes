package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.PurchaseInvoice;
import com.senmol.mes.plan.entity.PurchaseInvoiceMx;
import com.senmol.mes.plan.page.PurchaseInvoicePage;
import com.senmol.mes.plan.service.PurchaseInvoiceService;
import com.senmol.mes.plan.vo.PurchaseInvoiceVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购单开票(PurchaseInvoice)表控制层
 *
 * @author makejava
 * @since 2024-05-23 16:15:08
 */
@RestController
@RequestMapping("/plan/purchaseInvoice")
public class PurchaseInvoiceController {

    @Resource
    private PurchaseInvoiceService purchaseInvoiceService;

    /**
     * 查询数据
     *
     * @param page              分页对象
     * @param purchaseInvoiceVo 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(PurchaseInvoicePage page, PurchaseInvoiceVo purchaseInvoiceVo) {
        Long id = purchaseInvoiceVo.getId();
        if (ObjUtil.isNotNull(id)) {
            return this.purchaseInvoiceService.selectMx(id);
        }

        return this.purchaseInvoiceService.selectAll(page, purchaseInvoiceVo);
    }

    /**
     * 新增数据
     *
     * @param purchaseInvoice 实体对象
     * @return 新增结果
     */
    @Logger("采购开票新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody PurchaseInvoice purchaseInvoice) {
        return this.purchaseInvoiceService.insert(purchaseInvoice);
    }

    /**
     * 修改数据
     *
     * @param list 实体对象列表
     * @return 修改结果
     */
    @Logger("采购开票修改")
    @PutMapping
    public SaResult updateMx(@Validated(ParamsValidate.Update.class) @RequestBody List<PurchaseInvoiceMx> list) {
        return this.purchaseInvoiceService.modifyMx(list);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("采购开票删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.purchaseInvoiceService.delById(id);
    }

}

