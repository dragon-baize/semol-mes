package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.produce.entity.ProductEntity;
import com.senmol.mes.produce.service.ProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 产品管理(Product)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:00:52
 */
@Validated
@RestController
@RequestMapping("/produce/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 查询所有数据
     *
     * @param bomId 清单ID
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList(Integer source, Long bomId, Long supplierId) {
        return SaResult.data(this.productService.getList(source, bomId, supplierId));
    }

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param product 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<ProductEntity> page, ProductEntity product) {
        return this.productService.selectAll(page, product);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return this.productService.selectOne(id);
    }

    /**
     * 新增数据
     *
     * @param product 实体对象
     * @return 新增结果
     */
    @Logger("产品新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody ProductEntity product) {
        return this.productService.insertProduct(product);
    }

    /**
     * 修改数据
     *
     * @param product 实体对象
     * @return 修改结果
     */
    @Logger("产品修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody ProductEntity product) {
        return this.productService.updateProduct(product);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("产品删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.productService.deleteProduct(idList);
    }

}
