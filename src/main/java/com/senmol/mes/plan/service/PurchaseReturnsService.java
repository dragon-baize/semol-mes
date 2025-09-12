package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.PurchaseReturns;

import java.util.List;

/**
 * 采购退货(PlanPurchaseReturns)表服务接口
 *
 * @author makejava
 * @since 2024-05-17 09:29:36
 */
public interface PurchaseReturnsService extends IService<PurchaseReturns> {
    /**
     * 查询数据
     *
     * @param page 分页对象
     * @param purchaseReturns 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<PurchaseReturns> page, PurchaseReturns purchaseReturns);

    /**
     * 新增数据
     *
     * @param purchaseReturns 实体对象
     * @return 新增结果
     */
    SaResult insert(PurchaseReturns purchaseReturns);

    /**
     * 修改数据
     *
     * @param purchaseReturns 实体对象
     * @return 修改结果
     */
    SaResult modify(PurchaseReturns purchaseReturns);

    /**
     * 批量设置开票ID
     *
     * @param returnsIds 主键列表
     * @param invoice    开票ID
     */
    void setInvoices(List<Long> returnsIds, Long invoice);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult delete(Long id);

}

