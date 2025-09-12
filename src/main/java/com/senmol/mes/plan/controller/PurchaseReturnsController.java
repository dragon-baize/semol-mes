package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.PurchaseReturns;
import com.senmol.mes.plan.service.PurchaseReturnsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 采购退货(PlanPurchaseReturns)表控制层
 *
 * @author makejava
 * @since 2024-05-17 09:29:36
 */
@RestController
@RequestMapping("/plan/purchaseReturns")
public class PurchaseReturnsController {

    @Resource
    private PurchaseReturnsService purchaseReturnsService;

    /**
     * 查询数据
     *
     * @param page 分页对象
     * @param purchaseReturns 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<PurchaseReturns> page, PurchaseReturns purchaseReturns) {
        // 主键查询
        if (ObjectUtil.isNotNull(purchaseReturns.getId())) {
            return SaResult.data(this.purchaseReturnsService.getById(purchaseReturns.getId()));
        }

        return this.purchaseReturnsService.selectAll(page, purchaseReturns);
    }

    /**
     * 新增数据
     *
     * @param purchaseReturns 实体对象
     * @return 新增结果
     */
    @Logger("采购退货新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody PurchaseReturns purchaseReturns) {
        return this.purchaseReturnsService.insert(purchaseReturns);
    }

    /**
     * 修改数据
     *
     * @param purchaseReturns 实体对象
     * @return 修改结果
     */
    @Logger("采购退货修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody PurchaseReturns purchaseReturns) {
        return this.purchaseReturnsService.modify(purchaseReturns);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("采购退货删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.purchaseReturnsService.delete(id);
    }
}

