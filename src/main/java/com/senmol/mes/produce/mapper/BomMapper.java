package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.produce.entity.BomEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料清单(Bom)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:09
 */
public interface BomMapper extends BaseMapper<BomEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param bom  查询实体
     * @return 所有数据
     */
    Page<BomEntity> selectAll(Page<BomEntity> page, @Param("bom") BomEntity bom);

    /**
     * 查询未绑定清单的产品
     *
     * @param productIds 产品ID
     * @return 产品编号
     */
    List<String> unboundBomProduct(@Param("productIds") List<Long> productIds);

    /**
     * 查询物料对应产品
     *
     * @param materialId 物料ID
     * @return 产品
     */
    List<CountVo> getProductId(@Param("materialId") Long materialId);

}

