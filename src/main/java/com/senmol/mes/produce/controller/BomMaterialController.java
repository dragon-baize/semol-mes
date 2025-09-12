package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.produce.service.BomMaterialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 清单-物料(BomMaterial)表控制层
 *
 * @author makejava
 * @since 2023-02-06 19:09:15
 */
@RestController
@RequestMapping("/produce/bomMaterial")
public class BomMaterialController {

    @Resource
    private BomMaterialService bomMaterialService;

    /**
     * MRP产品查清单物料，预占用库存
     *
     * @param productId 产品ID
     * @return 清单物料列表
     */
    @GetMapping("getByProductId/{productId}")
    public SaResult getByProductId(@PathVariable("productId") Long productId) {
        return this.bomMaterialService.getByProductId(productId);
    }

    /**
     * 产品查清单物料
     *
     * @param productId 产品ID
     * @return 清单物料列表
     */
    @GetMapping("getBomMaterial/{productId}")
    public SaResult getBomMaterial(@PathVariable("productId") Long productId) {
        return this.bomMaterialService.getBomMaterial(productId);
    }

}

