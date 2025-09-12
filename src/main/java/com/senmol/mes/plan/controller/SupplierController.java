package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.Supplier;
import com.senmol.mes.plan.service.SupplierService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 供应商管理(PlanSupplier)表控制层
 *
 * @author makejava
 * @since 2024-05-16 13:27:35
 */
@RestController
@RequestMapping("/plan/supplier")
public class SupplierController {

    @Resource
    private SupplierService supplierService;
    /**
     * 查询数据
     *
     * @param page 分页对象
     * @param supplier 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<Supplier> page, Supplier supplier) {
        // 主键查询
        if (ObjectUtil.isNotNull(supplier.getId())) {
            return this.supplierService.selectById(supplier.getId());
        }

        return this.supplierService.selectPage(page, supplier);
    }

    /**
     * 新增数据
     *
     * @param supplier 实体对象
     * @return 新增结果
     */
    @Logger("供应商新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody Supplier supplier) {
        return this.supplierService.insert(supplier);
    }

    /**
     * 修改数据
     *
     * @param supplier 实体对象
     * @return 修改结果
     */
    @Logger("供应商修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody Supplier supplier) {
        return this.supplierService.modify(supplier);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("供应商删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.supplierService.delete(id);
    }
}

