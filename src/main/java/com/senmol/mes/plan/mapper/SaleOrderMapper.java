package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.plan.entity.SaleOrderEntity;
import com.senmol.mes.plan.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 销售订单(SaleOrder)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-13 13:29:46
 */
public interface SaleOrderMapper extends BaseMapper<SaleOrderEntity> {

    /**
     * 销售订单明细
     *
     * @param page 分页对象
     * @param pojo 查询参数
     * @return 结果
     */
    List<OrderMxVo> orderMx(Page<OrderMxVo> page, @Param("pojo") OrderMxPojo pojo);

    /**
     * 销售订单明细合计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pojo      查询参数
     * @return 结果
     */
    OrderMxVoTotal selectOrderMxTotal(@Param("startTime") LocalDate startTime,
                                      @Param("endTime") LocalDate endTime,
                                      @Param("pojo") OrderMxPojo pojo);

    /**
     * 任务单数据
     *
     * @param codes 销售订单编号列表
     * @return 任务单列表
     */
    List<OrderVo> workOrder(@Param("codes") Set<String> codes);

    /**
     * 工单质检数据
     *
     * @param ids 任务单ID列表
     * @return 工单质检数据列表
     */
    List<OrderMxInspectVo> orderMxInspect(@Param("ids") Set<Long> ids);

    /**
     * 查询发货单明细
     *
     * @param page 分页对象
     * @param pojo 查询参数
     * @return 发货单明细列表
     */
    Page<DeliveryVo> getDelivery(Page<DeliveryVo> page, @Param("pojo") OrderMxPojo pojo);

    /**
     * 查询退货单明细
     *
     * @param page 分页对象
     * @param pojo 查询参数
     * @return 退货单明细列表
     */
    Page<DeliveryVo> getReturns(Page<DeliveryVo> page, @Param("pojo") OrderMxPojo pojo);

    /**
     * 查询发货/退货单明细
     *
     * @param page 分页对象
     * @param pojo 查询参数
     * @return 发货/退货单明细列表
     */
    List<DeliveryVo> deliveryMx(Page<DeliveryVo> page, @Param("pojo") OrderMxPojo pojo);

    /**
     * 查询发货/退货单明细合计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pojo      查询参数
     * @param type      类型
     * @return 合计数据
     */
    DeliveryVoTotal selectDeliveryTotal(@Param("startTime") LocalDate startTime,
                                        @Param("endTime") LocalDate endTime,
                                        @Param("pojo") OrderMxPojo pojo,
                                        @Param("type") Integer type);

    /**
     * 统计当天生成的数据量
     *
     * @return 条数
     */
    @Select("SELECT count(*) FROM plan_sale_order WHERE DATE_FORMAT(create_time, '%Y-%m-%d') = DATE_FORMAT(NOW(), " +
            "'%Y-%m-%d')")
    int count();

}

