package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.PurchaseInvoice;
import com.senmol.mes.plan.page.PurchaseInvoicePage;
import com.senmol.mes.plan.vo.PurchaseInvoiceMxVo;
import com.senmol.mes.plan.vo.PurchaseInvoiceTotal;
import com.senmol.mes.plan.vo.PurchaseInvoiceVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 采购单开票(PurchaseInvoice)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-23 16:15:09
 */
public interface PurchaseInvoiceMapper extends BaseMapper<PurchaseInvoice> {

    /**
     * 分页查询
     *
     * @param page              分页对象
     * @param purchaseInvoiceVo 查询条件
     * @return 分页数据
     */
    List<PurchaseInvoiceVo> selectAll(@Param("page") PurchaseInvoicePage page,
                                      @Param("vo") PurchaseInvoiceVo purchaseInvoiceVo);

    /**
     * 合计统计
     *
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @param purchaseInvoiceVo 查询实体
     * @return 所有数据
     */
    PurchaseInvoiceTotal selectTotal(@Param("startTime") LocalDate startTime,
                                     @Param("endTime") LocalDate endTime,
                                     @Param("vo") PurchaseInvoiceVo purchaseInvoiceVo);

    /**
     * 查询采购开票明细
     *
     * @param pid 开票pid
     * @return 结果
     */
    List<PurchaseInvoiceMxVo> getInvoiceMx(@Param("pid") Long pid);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM plan_purchase_invoice WHERE DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

