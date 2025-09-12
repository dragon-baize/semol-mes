package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.ProductEntity;

import java.util.List;

/**
 * 产品管理(Product)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface ProductService extends IService<ProductEntity> {

    /**
     * 查询所有数据
     *
     * @param source     来源
     * @param bomId      清单ID
     * @param supplierId 供应商ID
     * @return 所有数据
     */
    List<ProductEntity> getList(Integer source, Long bomId, Long supplierId);

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param product 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<ProductEntity> page, ProductEntity product);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    SaResult selectOne(Long id);

    /**
     * 新增数据
     *
     * @param product 实体对象
     * @return 新增结果
     */
    SaResult insertProduct(ProductEntity product);

    /**
     * 修改数据
     *
     * @param product 实体对象
     * @return 修改结果
     */
    SaResult updateProduct(ProductEntity product);

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    SaResult deleteProduct(Long id);

}

