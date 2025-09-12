package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.CustomEntity;
import com.senmol.mes.plan.service.CustomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户管理(Custom)表控制层
 *
 * @author makejava
 * @since 2023-07-13 16:19:31
 */
@Validated
@RestController
@RequestMapping("/plan/custom")
public class CustomController {

    @Resource
    private CustomService customService;

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据，size=-1时不分页显示所有数据
     *
     * @param page   分页对象
     * @param custom 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<CustomEntity> page, CustomEntity custom) {
        // 主键查询
        if (ObjectUtil.isNotNull(custom.getId())) {
            return SaResult.data(this.customService.selectOne(custom.getId()));
        }

        // 查询所有
        if (page.getSize() == -1) {
            List<CustomEntity> list = this.customService.lambdaQuery(custom).list();
            return SaResult.data(list);
        }

        // 分页
        return SaResult.data(this.customService.selectAll(page, custom));
    }

    /**
     * 产品ID查客户
     *
     * @param productId 产品ID
     * @return 客户
     */
    @GetMapping("getByProductId/{productId}")
    public SaResult getByProductId(@PathVariable("productId") Long productId) {
        return SaResult.data(this.customService.getByProductId(productId));
    }

    /**
     * 新增数据
     *
     * @param custom 实体对象
     * @return 新增结果
     */
    @Logger("客户新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody CustomEntity custom) {
        return this.customService.insertCustom(custom);
    }

    /**
     * 修改数据
     *
     * @param custom 实体对象
     * @return 修改结果
     */
    @Logger("客户修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody CustomEntity custom) {
        return this.customService.updateCustom(custom);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("客户删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.customService.deleteCustom(id);
    }
}

