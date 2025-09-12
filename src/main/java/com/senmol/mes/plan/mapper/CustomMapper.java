package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.CustomEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户管理(Custom)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-13 16:18:45
 */
public interface CustomMapper extends BaseMapper<CustomEntity> {

    /**
     * 产品ID查客户
     *
     * @param productId 产品ID
     * @return 客户
     */
    List<CustomEntity> getByProductId(@Param("productId") Long productId);
}

