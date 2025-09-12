package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.SaleOrderEntity;
import com.senmol.mes.plan.page.DeliveryVoPage;
import com.senmol.mes.plan.page.OrderMxPage;
import com.senmol.mes.plan.vo.OrderMxPojo;

/**
 * 销售订单(SaleOrder)表服务接口
 *
 * @author makejava
 * @since 2023-03-13 13:29:46
 */
public interface SaleOrderService extends IService<SaleOrderEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param saleOrder 查询实体
     * @param isMrp     是否已MRP
     * @return 所有数据
     */
    SaResult selectAll(Page<SaleOrderEntity> page, SaleOrderEntity saleOrder, Integer isMrp);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    SaResult selectOne(Long id);

    /**
     * 销售订单明细
     *
     * @param page 分页对象
     * @param pojo 查询参数
     * @return 结果
     */
    SaResult orderMx(OrderMxPage page, OrderMxPojo pojo);

    /**
     * 销售发货明细
     *
     * @param page 分页对象
     * @param pojo 查询参数
     * @return 结果
     */
    SaResult deliveryMx(DeliveryVoPage page, OrderMxPojo pojo);

    /**
     * 新增数据
     *
     * @param saleOrder 实体对象
     * @return 新增结果
     */
    SaResult insertSaleOrder(SaleOrderEntity saleOrder);

    /**
     * 修改数据
     *
     * @param saleOrder 实体对象
     * @return 修改结果
     */
    SaResult updateSaleOrder(SaleOrderEntity saleOrder);

    /**
     * 关单
     *
     * @param id        销售订单ID
     * @param productId 产品ID
     * @return 结果
     */
    SaResult closeOrder(Long id, Long productId);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteSaleOrder(Long id);

}

