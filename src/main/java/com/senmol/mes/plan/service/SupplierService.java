package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.Supplier;

/**
 * 供应商管理(PlanSupplier)表服务接口
 *
 * @author makejava
 * @since 2024-05-16 13:27:35
 */
public interface SupplierService extends IService<Supplier> {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    SaResult selectById(Long id);

    /**
     * 分页查询
     *
     * @param page     分页对象
     * @param supplier 实体对象
     * @return 分页数据
     */
    SaResult selectPage(Page<Supplier> page, Supplier supplier);

    /**
     * 新增数据
     *
     * @param supplier 实体对象
     * @return 新增结果
     */
    SaResult insert(Supplier supplier);

    /**
     * 修改数据
     *
     * @param supplier 实体对象
     * @return 修改结果
     */
    SaResult modify(Supplier supplier);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult delete(Long id);

}

