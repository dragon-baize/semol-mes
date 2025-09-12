package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.produce.service.ProductLineService;
import com.senmol.mes.produce.utils.ProFromRedis;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 产线管理(ProductLine)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:00:52
 */
@Validated
@RestController
@RequestMapping("/produce/productLine")
public class ProductLineController {

    @Resource
    private ProductLineService productLineService;
    @Resource
    private ProFromRedis proFromRedis;

    /**
     * 产线查工位、工序
     *
     * @param id 产线ID
     * @return 工位、工序列表
     */
    @GetMapping("selectById/{id}")
    public SaResult selectById(@PathVariable("id") Long id) {
        return SaResult.data(this.productLineService.selectById(id));
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        return SaResult.data(this.proFromRedis.getLineList());
    }

    /**
     * 查人员所在产线
     *
     * @param userId 人员ID
     * @return 产线数据
     */
    @GetMapping("getByUserId/{userId}")
    public SaResult getByUserId(@PathVariable("userId") Long userId) {
        return SaResult.data(this.productLineService.getByUserId(userId));
    }

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param productLine 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<ProductLineEntity> page, ProductLineEntity productLine) {
        return SaResult.data(this.productLineService.selectAll(page, productLine));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.productLineService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param productLine 实体对象
     * @return 新增结果
     */
    @Logger("产线新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody ProductLineEntity productLine) {
        return this.productLineService.insertLine(productLine);
    }

    /**
     * 修改数据
     *
     * @param productLine 实体对象
     * @return 修改结果
     */
    @Logger("产线修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody ProductLineEntity productLine) {
        return this.productLineService.updateLine(productLine);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("产线删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.productLineService.deleteLine(idList);
    }

}

