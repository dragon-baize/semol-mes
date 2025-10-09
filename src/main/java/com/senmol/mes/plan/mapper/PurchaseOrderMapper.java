package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.PurchaseOrderEntity;
import com.senmol.mes.plan.page.PurchaseOrderPage;
import com.senmol.mes.plan.page.RestockPage;
import com.senmol.mes.plan.vo.PurchaseOrderTotal;
import com.senmol.mes.plan.vo.PurchaseOrderVo;
import com.senmol.mes.plan.vo.Restock;
import com.senmol.mes.plan.vo.RestockTotal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 采购单(PurchaseOrder)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-13 09:25:53
 */
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrderEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page          分页对象
     * @param purchaseOrder 查询实体
     * @return 所有数据
     */
    List<PurchaseOrderVo> selectAll(@Param("page") PurchaseOrderPage page,
                                    @Param("po") PurchaseOrderEntity purchaseOrder);

    /**
     * 合计统计
     *
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param purchaseOrder 查询实体
     * @return 所有数据
     */
    PurchaseOrderTotal selectTotal(@Param("startTime") LocalDate startTime,
                                   @Param("endTime") LocalDate endTime,
                                   @Param("po") PurchaseOrderEntity purchaseOrder);

    /**
     * 查询采购信息
     *
     * @param siCode 检测号
     * @return 结果
     */
    PurchaseOrderEntity getBySiCode(@Param("siCode") String siCode);

    /**
     * 收退货分页
     *
     * @param page    分页对象
     * @param restock 查询实体
     * @return 分页数据
     */
    List<Restock> restockAll(@Param("page") RestockPage page, @Param("args") Restock restock);

    /**
     * 收退货合计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param restock   查询实体
     * @return 合计
     */
    RestockTotal restockTotal(@Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime, @Param("args") Restock restock);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM plan_purchase_order WHERE DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

