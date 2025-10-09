package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.OutboundMaterial;
import com.senmol.mes.plan.entity.OutsourceEntity;
import com.senmol.mes.plan.vo.ProductQty;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 委外计划(Outsource)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
public interface OutsourceMapper extends BaseMapper<OutsourceEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param outsource 查询实体
     * @return 所有数据
     */
    List<OutsourceEntity> selectAll(Page<OutsourceEntity> page, @Param("out") OutsourceEntity outsource);

    /**
     * 通过计划编号查询单条数据
     *
     * @param code 计划编号
     * @return 单条数据
     */
    List<OutboundMaterial> getByCode(@Param("code") String code);

    /**
     * 计划编号查询清单物料
     *
     * @param code 计划编号
     * @return 清单物料
     */
    List<OutboundMaterial> getBaseQty(@Param("code") String code);

    OutsourceEntity getBySiCode(@Param("siCode") String siCode);

    /**
     * 查询在制委外量
     */
    List<ProductQty> inOutsource(@Param("productIds") List<Long> productIds, @Param("saleOrderId") Long saleOrderId);

    /**
     * 查询委外计划信息
     *
     * @param productIds 产品ID
     * @return 委外计划信息
     */
    List<OutsourceEntity> getByCpId(@Param("productIds") List<Long> productIds);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM plan_outsource WHERE DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

