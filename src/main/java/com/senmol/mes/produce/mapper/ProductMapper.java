package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.produce.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品管理(Product)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface ProductMapper extends BaseMapper<ProductEntity> {

    /**
     * 查询所有数据
     *
     * @param source     数据来源
     * @param bomId      清单ID
     * @param supplierId 供应商ID
     * @return 所有数据
     */
    List<ProductEntity> getList(@Param("source") Integer source,
                                @Param("bomId") Long bomId,
                                @Param("supplierId") Long supplierId);

}

