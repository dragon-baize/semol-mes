package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.PurchaseOrderEntity;
import com.senmol.mes.plan.page.PurchaseOrderPage;
import com.senmol.mes.plan.page.RestockPage;
import com.senmol.mes.plan.service.PurchaseOrderService;
import com.senmol.mes.plan.vo.Restock;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 采购订单(PurchaseOrder)表控制层
 *
 * @author makejava
 * @since 2023-03-13 09:25:53
 */
@Validated
@RestController
@RequestMapping("/plan/purchaseOrder")
public class PurchaseOrderController {

    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 分页查询所有数据
     *
     * @param page          分页对象
     * @param purchaseOrder 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(PurchaseOrderPage page, PurchaseOrderEntity purchaseOrder) {
        return this.purchaseOrderService.selectAll(page, purchaseOrder);
    }

    /**
     * 收退货明细
     *
     * @param page    分页对象
     * @param restock 查询实体
     * @return 分页数据
     */
    @GetMapping("restock")
    public SaResult restock(RestockPage page, Restock restock) {
        return this.purchaseOrderService.restock(page, restock);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.purchaseOrderService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param purchaseOrder 实体对象
     * @return 新增结果
     */
    @Logger("采购单新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody PurchaseOrderEntity purchaseOrder) {
        return this.purchaseOrderService.insertPurchaseOrder(purchaseOrder);
    }

    /**
     * 修改数据
     *
     * @param purchaseOrder 实体对象
     * @return 修改结果
     */
    @Logger("采购单修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody PurchaseOrderEntity purchaseOrder) {
        return this.purchaseOrderService.updatePurchaseOrder(purchaseOrder);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("采购单删除")
    @DeleteMapping
    public SaResult delete(@NotEmpty(message = "主键列表为空") @RequestParam("idList") List<Long> idList) {
        return this.purchaseOrderService.deletePurchaseOrder(idList);
    }

}
