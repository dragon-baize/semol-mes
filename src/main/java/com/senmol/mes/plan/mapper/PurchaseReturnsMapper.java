package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.plan.entity.PurchaseReturns;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 采购退货(PlanPurchaseReturns)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-17 09:29:36
 */
public interface PurchaseReturnsMapper extends BaseMapper<PurchaseReturns> {
    /**
     * 查询数据
     *
     * @param page            分页对象
     * @param purchaseReturns 查询实体
     * @return 所有数据
     */
    List<PurchaseReturns> selectAll(Page<PurchaseReturns> page, @Param("pr") PurchaseReturns purchaseReturns);

    /**
     * 批量设置开票ID
     *
     * @param returnsIds 主键列表
     * @param invoice    开票ID
     */
    void setInvoices(@Param("returnsIds") List<Long> returnsIds, @Param("invoice") Long invoice);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM plan_purchase_returns WHERE DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

