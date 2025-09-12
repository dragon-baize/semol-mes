package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.BomMaterialEntity;

/**
 * 清单-物料(BomMaterial)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface BomMaterialService extends IService<BomMaterialEntity> {

    /**
     * 产品查清单物料
     *
     * @param productId 产品ID
     * @return 物料列表
     */
    SaResult getByProductId(Long productId);

    /**
     * 产品查清单物料
     *
     * @param productId 产品ID
     * @return 清单物料列表
     */
    SaResult getBomMaterial(Long productId);

}

