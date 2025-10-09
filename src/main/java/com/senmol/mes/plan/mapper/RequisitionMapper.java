package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.RequisitionEntity;
import com.senmol.mes.plan.page.RequisitionPage;
import com.senmol.mes.plan.vo.RequisitionVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 请购单(Requisition)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-13 15:57:22
 */
public interface RequisitionMapper extends BaseMapper<RequisitionEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param requisition 查询实体
     * @param keyword     关键字
     * @return 所有数据
     */
    List<RequisitionVo> selectAll(@Param("page") RequisitionPage page,
                                  @Param("re") RequisitionEntity requisition,
                                  @Param("keyword") String keyword);

    /**
     * 统计分页数据
     *
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param requisition 查询实体
     * @param keyword     关键字
     * @return 统计结果
     */
    BigDecimal selectTotal(@Param("startTime") LocalDate startTime,
                           @Param("endTime") LocalDate endTime,
                           @Param("re") RequisitionEntity requisition,
                           @Param("keyword") String keyword);

    /**
     * 获取未绑定的请购单
     *
     * @return 列表
     */
    List<RequisitionEntity> getUnbound();

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM plan_requisition WHERE DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

