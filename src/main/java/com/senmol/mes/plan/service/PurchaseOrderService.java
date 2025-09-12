package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.PurchaseOrderEntity;
import com.senmol.mes.plan.page.PurchaseOrderPage;
import com.senmol.mes.plan.page.RestockPage;
import com.senmol.mes.plan.vo.PurchaseOrderVo;
import com.senmol.mes.plan.vo.Restock;

import java.util.List;

/**
 * 采购单(PurchaseOrder)表服务接口
 *
 * @author makejava
 * @since 2023-03-13 09:25:53
 */
public interface PurchaseOrderService extends IService<PurchaseOrderEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page          分页对象
     * @param purchaseOrder 查询实体
     * @return 所有数据
     */
    SaResult selectAll(PurchaseOrderPage page, PurchaseOrderEntity purchaseOrder);

    /**
     * 收退货明细
     *
     * @param page    分页对象
     * @param restock 查询实体
     * @return 分页数据
     */
    SaResult restock(RestockPage page, Restock restock);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    PurchaseOrderVo selectOne(Long id);

    /**
     * 新增数据
     *
     * @param purchaseOrder 实体对象
     * @return 新增结果
     */
    SaResult insertPurchaseOrder(PurchaseOrderEntity purchaseOrder);

    /**
     * 修改数据
     *
     * @param purchaseOrder 实体对象
     * @return 修改结果
     */
    SaResult updatePurchaseOrder(PurchaseOrderEntity purchaseOrder);

    /**
     * 查询采购信息
     *
     * @param siCode 检测号
     * @return 结果
     */
    PurchaseOrderEntity getBySiCode(String siCode);

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    SaResult deletePurchaseOrder(List<Long> idList);

}

