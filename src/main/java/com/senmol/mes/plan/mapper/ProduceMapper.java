package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.vo.ProduceVo;
import com.senmol.mes.plan.vo.ProductQty;
import com.senmol.mes.workorder.vo.MaterialPojo;
import com.senmol.mes.workorder.vo.ProductLineInfo;
import com.senmol.mes.workorder.vo.WorkOrderPojo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 生产计划(Produce)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
public interface ProduceMapper extends BaseMapper<ProduceEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param produce 查询实体
     * @return 所有数据
     */
    List<ProduceVo> selectAll(Page<ProduceVo> page, @Param("p") ProduceEntity produce);

    /**
     * 查询产线计划产品
     *
     * @param productLineId 产线ID
     * @return 结果
     */
    List<ProduceVo> byProductLineId(@Param("productLineId") Long productLineId);

    /**
     * 产品计划生产总量
     *
     * @param productIds  产品ID列表
     * @param saleOrderId 销售订单ID
     * @return 总量
     */
    List<ProductQty> getSumQty(@Param("productIds") List<Long> productIds, @Param("saleOrderId") Long saleOrderId);

    /**
     * 主键查询
     *
     * @param id 产品ID
     * @return 单个产品详情
     */
    ProduceVo getOne(@Param("id") Long id);

    /**
     * 查询任务单对应计划信息
     *
     * @param ids 任务单ID
     * @return 计划信息
     */
    List<MaterialPojo> getPlanInfo(@Param("ids") Set<Long> ids);

    /**
     * 产品在制生产量
     *
     * @param productIds 产品ID列表
     * @return 结果
     */
    List<ProductQty> getProductionQty(@Param("productIds") List<Long> productIds);

    /**
     * 工作台产线列表
     *
     * @param productLineId 产线ID
     * @return 产线列表
     */
    List<ProductLineInfo> statOrder(@Param("productLineId") Long productLineId);

    /**
     * 工单编号查生产计划数据
     *
     * @param mxCode 工单编号
     * @return 生产计划数据
     */
    WorkOrderPojo getByMxCode(@Param("mxCode") String mxCode);

    /**
     * 工单ID查生产计划数据
     *
     * @param mxId 工单ID
     * @return 生产计划数据
     */
    WorkOrderPojo getByMxId(Long mxId);

    /**
     * 查询未完成生产计划总量
     *
     * @return 总量
     */
    BigDecimal getAllTotal();

}

