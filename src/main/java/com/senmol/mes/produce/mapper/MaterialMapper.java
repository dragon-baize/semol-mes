package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.entity.MaterialEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料管理(Material)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface MaterialMapper extends BaseMapper<MaterialEntity> {

    /**
     * 查询所有数据
     *
     * @param supplierId 供应商ID
     * @return 所有数据
     */
    List<MaterialVo> getList(@Param("supplierId") Long supplierId,
                             @Param("type") Integer type);

}

