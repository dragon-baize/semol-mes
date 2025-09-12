package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.plan.entity.MrpEntity;
import com.senmol.mes.plan.vo.MaterialVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * MRP(Mrp)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-15 11:18:11
 */
public interface MrpMapper extends BaseMapper<MrpEntity> {

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param mrp  实体对象
     * @return 对象列表
     */
    List<MrpEntity> selectAll(Page<MrpEntity> page, @Param("mrp") MrpEntity mrp);

    /**
     * 统计未生成的生产计划
     *
     * @param mrpId mrp_id
     * @return 数量
     */
    Integer getProduceCount(@Param("mrpId") Long mrpId);

    /**
     * 统计未生成的请购单
     *
     * @param mrpId mrp_id
     * @return 数量
     */
    Integer getRequisitionCount(@Param("mrpId") Long mrpId);

    /**
     * 统计委外计划
     *
     * @param mrpId mrp-id
     * @return 数量
     */
    Integer getOutsourceCount(@Param("mrpId") Long mrpId);

    /**
     * 统计工单
     *
     * @param mrpId mrp-id
     * @return 数量
     */
    List<CommonPojo> getWorkOrderCount(@Param("mrpId") Long mrpId);

    List<MaterialVo> unSaleOrder();

    List<MaterialVo> getByProductIds(@Param("map") Map<Long, BigDecimal> map);

    List<MaterialVo> unWorkOrder();

    List<MaterialVo> materialQty(@Param("productId") Long productId);

    /**
     * 删除生产计划
     *
     * @param mrpId mrp-id
     */
    void delProduces(@Param("mrpId") Long mrpId);

    /**
     * 删除请购单
     *
     * @param mrpId mrp-id
     */
    void delRequisitions(@Param("mrpId") Long mrpId);

    /**
     * 删除委外计划
     *
     * @param mrpId mrp-id
     */
    void delOutsource(@Param("mrpId") Long mrpId);

    /**
     * 删除任务单物料
     */
    void delWorkOrderMaterial(@Param("ids") List<Long> ids);

    /**
     * 删除工单物料
     */
    void delWorkOrderMxMaterial(@Param("ids") List<Long> ids);

    /**
     * 删除工单
     */
    void delWorkOrderMx(@Param("ids") List<Long> ids);

}

