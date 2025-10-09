package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.entity.OutboundMxEntity;
import com.senmol.mes.plan.vo.OutboundVo;
import com.senmol.mes.plan.vo.ProductQty;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 出库单(Outbound)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-13 10:21:09
 */
public interface OutboundMapper extends BaseMapper<OutboundEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param outbound 查询实体
     * @return 所有数据
     */
    List<OutboundVo> selectAll(Page<OutboundVo> page, @Param("ob") OutboundEntity outbound);

    /**
     * 销售未完成订单量
     *
     * @param productIds 产品ID列表
     * @return 产品-数量
     */
    List<ProductQty> getSumQty(@Param("productIds") List<Long> productIds);

    /**
     * 查询出库单明细
     *
     * @param id   出库单ID
     * @param sign 标识
     * @return 出库单明细
     */
    List<OutboundMxEntity> getMxs(@Param("id") Long id, @Param("sign") Integer sign);

    /**
     * 查询物料已出库数量
     *
     * @param codes 工单编号
     * @param ids   物料ID
     * @return 物料已出库数量
     */
    List<CountVo> getOutBoundQty(@Param("codes") Set<String> codes, @Param("ids") Set<Long> ids);

    /**
     * 查询物料已出库数量
     *
     * @param codes      工单编号
     * @param materialId 物料ID
     * @return 物料已出库数量
     */
    List<CountVo> getObQty(@Param("codes") Set<String> codes, @Param("materialId") Long materialId);

    /**
     * 查询物料委外出库单未出库数量
     *
     * @param ids 物料ID
     * @return MRP占用库存
     */
    List<CountVo> getOutsourceQty(@Param("ids") Set<Long> ids);

    /**
     * 查询物料委外出库单未出库数量
     *
     * @param materialId 物料ID
     * @return MRP占用库存
     */
    List<CountVo> getOsQty(@Param("materialId") Long materialId);

    /**
     * 新增数据
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int saveOutbound(@Param("entity") OutboundEntity entity);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM plan_outbound WHERE DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

