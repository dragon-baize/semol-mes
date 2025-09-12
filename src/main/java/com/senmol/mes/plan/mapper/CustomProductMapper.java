package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.CustomProductEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户-产品(CustomProduct)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-14 15:31:26
 */
public interface CustomProductMapper extends BaseMapper<CustomProductEntity> {

    /**
     * 出库单号查询客户产品
     *
     * @param outBoundCode 出库单号
     * @return 客户产品
     */
    List<CustomProductEntity> getByOutBoundCode(@Param("outBoundCode") String outBoundCode);

}

