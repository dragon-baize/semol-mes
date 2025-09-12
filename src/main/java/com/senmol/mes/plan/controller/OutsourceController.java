package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.OutsourceEntity;
import com.senmol.mes.plan.service.OutsourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 委外计划(Outsource)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
@Validated
@RestController
@RequestMapping("/plan/outsource")
public class OutsourceController {

    @Resource
    private OutsourceService outsourceService;

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param outsource 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<OutsourceEntity> page, OutsourceEntity outsource) {
        // 查询所有
        if (page.getSize() == -1) {
            List<OutsourceEntity> list = this.outsourceService.lambdaQuery(outsource).list();
            return SaResult.data(list);
        }

        return this.outsourceService.selectAll(page, outsource);
    }

    /**
     * 查询在制委外量
     */
    @GetMapping("inOutsource")
    public SaResult inOutsource(@RequestParam("productIds") List<Long> productIds, @RequestParam("saleOrderId") Long saleOrderId) {
        return this.outsourceService.inOutsource(productIds, saleOrderId);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.outsourceService.getById(id));
    }

    /**
     * 通过计划编号查询单条数据
     *
     * @param code 计划编号
     * @return 单条数据
     */
    @GetMapping("getByCode/{code}")
    public SaResult getByCode(@PathVariable("code") String code) {
        return SaResult.data(this.outsourceService.getByCode(code));
    }

    /**
     * 新增数据
     *
     * @param outsource 实体对象
     * @return 新增结果
     */
    @Logger("委外计划新增")
    @PostMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.OutsourceEntity", table = "委外计划")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody OutsourceEntity outsource) {
        return this.outsourceService.insertOutsource(outsource);
    }

    /**
     * 批量新增
     *
     * @param outsources 实体对象
     * @return 新增结果
     */
    @Logger("委外计划批量新增")
    @PostMapping("batch")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody List<OutsourceEntity> outsources) {
        return this.outsourceService.insertOutsources(outsources);
    }

    /**
     * 修改数据
     *
     * @param outsource 实体对象
     * @return 修改结果
     */
    @Logger("委外计划修改")
    @PutMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.OutsourceEntity", table = "委外计划")
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody OutsourceEntity outsource) {
        return this.outsourceService.updateOutsource(outsource);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("委外计划删除")
    @DeleteMapping
    @FlowCache(table = "委外计划", isAdd = false)
    public SaResult delete(@RequestParam("id") Long id) {
        return this.outsourceService.deleteOutsource(id);
    }
}

