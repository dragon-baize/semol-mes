package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.produce.entity.BomMaterialEntity;
import com.senmol.mes.produce.vo.MaterialInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 清单-物料(BomMaterial)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface BomMaterialMapper extends BaseMapper<BomMaterialEntity> {

    /**
     * 产品查清单物料
     *
     * @param productId 产品ID
     * @return 物料列表
     */
    List<MaterialInfo> getByProductId(@Param("productId") Long productId);

}

