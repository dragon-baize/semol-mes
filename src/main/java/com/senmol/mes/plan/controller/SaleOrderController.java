package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.SaleOrderEntity;
import com.senmol.mes.plan.page.DeliveryVoPage;
import com.senmol.mes.plan.page.OrderMxPage;
import com.senmol.mes.plan.service.SaleOrderService;
import com.senmol.mes.plan.vo.OrderMxPojo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售订单(SaleOrder)表控制层
 *
 * @author makejava
 * @since 2023-03-13 13:29:46
 */
@RestController
@RequestMapping("/plan/saleOrder")
public class SaleOrderController {

    @Resource
    private SaleOrderService saleOrderService;

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param saleOrder 查询实体
     * @param isMrp     是否已MRP
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<SaleOrderEntity> page, SaleOrderEntity saleOrder, Integer isMrp) {
        if (page.getSize() == -1) {
            List<SaleOrderEntity> list = this.saleOrderService.lambdaQuery(saleOrder).list();
            return SaResult.data(list);
        }

        return this.saleOrderService.selectAll(page, saleOrder, isMrp);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return this.saleOrderService.selectOne(id);
    }

    /**
     * 销售订单明细
     *
     * @param page 分页对象
     * @param pojo 查询参数
     * @return 结果
     */
    @GetMapping("orderMx")
    public SaResult orderMx(OrderMxPage page, OrderMxPojo pojo) {
        return this.saleOrderService.orderMx(page, pojo);
    }

    /**
     * 销售发货明细
     *
     * @param page 分页对象
     * @param pojo 查询参数
     * @return 结果
     */
    @GetMapping("deliveryMx")
    public SaResult deliveryMx(DeliveryVoPage page, OrderMxPojo pojo) {
        return this.saleOrderService.deliveryMx(page, pojo);
    }

    /**
     * 新增数据
     *
     * @param saleOrder 实体对象
     * @return 新增结果
     */
    @Logger("销售订单新增")
    @PostMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.SaleOrderEntity", table = "销售/备货订单")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody SaleOrderEntity saleOrder) {
        return this.saleOrderService.insertSaleOrder(saleOrder);
    }

    /**
     * 修改数据
     *
     * @param saleOrder 实体对象
     * @return 修改结果
     */
    @Logger("销售订单修改")
    @PutMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.SaleOrderEntity", table = "销售/备货订单")
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody SaleOrderEntity saleOrder) {
        return this.saleOrderService.updateSaleOrder(saleOrder);
    }

    /**
     * 关单
     *
     * @param id        销售订单ID
     * @param productId 产品ID
     * @return 结果
     */
    @Logger("销售订单关单")
    @PutMapping("closeOrder/{id}/{productId}")
    public SaResult closeOrder(@PathVariable("id") Long id, @PathVariable("productId") Long productId) {
        return this.saleOrderService.closeOrder(id, productId);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("销售订单删除")
    @DeleteMapping
    @FlowCache(table = "销售/备货订单", isAdd = false)
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.saleOrderService.deleteSaleOrder(idList);
    }
}

