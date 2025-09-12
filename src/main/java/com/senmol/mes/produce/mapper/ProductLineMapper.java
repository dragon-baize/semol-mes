package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.produce.vo.WorkOrderMxProcessVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产线管理(ProductLine)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
public interface ProductLineMapper extends BaseMapper<ProductLineEntity> {

    /**
     * 查人员所在产线
     *
     * @param userId 人员ID
     * @return 产线数据
     */
    List<ProductLineEntity> getByUserId(@Param("userId") Long userId);

    /**
     * 查询工作台实际数量
     *
     * @return 实际数量
     */
    BigDecimal getAllTotal();

    /**
     * 产线查工单工序
     *
     * @param productLineId 产线ID
     * @return 工单工序
     */
    List<WorkOrderMxProcessVo> processStatus(@Param("productLineId") Long productLineId);

}

