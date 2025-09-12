package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.SaleInvoice;
import com.senmol.mes.plan.entity.SaleInvoiceMx;
import com.senmol.mes.plan.page.SaleInvoicePage;
import com.senmol.mes.plan.vo.SaleInvoicePojo;
import com.senmol.mes.plan.vo.SaleInvoiceTotal;
import com.senmol.mes.plan.vo.SaleInvoiceVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 销售发货单开票(SaleInvoice)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-23 14:28:01
 */
public interface SaleInvoiceMapper extends BaseMapper<SaleInvoice> {

    /**
     * 开票查看
     *
     * @param id 主键
     * @return 结果
     */
    List<SaleInvoicePojo> getOne(@Param("id") Long id);

    /**
     * 分页查询
     *
     * @param page          分页对象
     * @param saleInvoiceVo 查询实体
     * @return 分页数据
     */
    List<SaleInvoiceVo> selectAll(@Param("page") SaleInvoicePage page, @Param("vo") SaleInvoiceVo saleInvoiceVo);

    /**
     * 合计统计
     *
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param saleInvoiceVo 查询实体
     * @return 所有数据
     */
    SaleInvoiceTotal selectTotal(@Param("startTime") LocalDate startTime,
                                 @Param("endTime") LocalDate endTime,
                                 @Param("vo") SaleInvoiceVo saleInvoiceVo);

    /**
     * 校验是否存在已开票数据
     *
     * @param mxs 开票明细列表
     * @return 已开票数
     */
    Long check(@Param("mxs") List<SaleInvoiceMx> mxs);

}

