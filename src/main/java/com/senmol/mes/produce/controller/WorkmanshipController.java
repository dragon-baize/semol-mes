package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.produce.entity.WorkmanshipEntity;
import com.senmol.mes.produce.service.WorkmanshipService;
import com.senmol.mes.produce.vo.WorkmanshipPojo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 生产工艺(Workmanship)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:01:39
 */
@Validated
@RestController
@RequestMapping("/produce/workmanship")
public class WorkmanshipController {

    @Resource
    private WorkmanshipService workmanshipService;

    /**
     * 获取产品未绑定的工艺
     *
     * @param productLineId 产线ID
     * @return 工艺列表
     */
    @GetMapping("getUnboundList")
    public SaResult getUnboundList(@RequestParam("productLineId") Long productLineId) {
        return SaResult.data(this.workmanshipService.getUnboundList(productLineId));
    }

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param workmanship 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<WorkmanshipPojo> page, WorkmanshipEntity workmanship) {
        return this.workmanshipService.selectAll(page, workmanship);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return this.workmanshipService.selectOne(id);
    }

    /**
     * 新增数据
     *
     * @param pojo 实体对象
     * @return 新增结果
     */
    @Logger("工艺新增")
    @PostMapping
    @FlowCache(entity = "com.senmol.mes.produce.entity.WorkmanshipEntity", table = "生产工艺")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody WorkmanshipPojo pojo) {
        return this.workmanshipService.insertWorkmanship(pojo);
    }

    /**
     * 修改数据
     *
     * @param pojo 实体对象
     * @return 修改结果
     */
    @Logger("工艺修改")
    @PutMapping
    @FlowCache(entity = "com.senmol.mes.produce.entity.WorkmanshipEntity", table = "生产工艺")
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody WorkmanshipPojo pojo) {
        return this.workmanshipService.updateWorkmanship(pojo);
    }

    /**
     * 复制数据
     *
     * @param id 被复制数据的主键
     * @return 复制结果
     */
    @Logger("工艺复制")
    @PutMapping("copy/{id}")
    public SaResult copy(@PathVariable Long id) {
        return this.workmanshipService.copyWorkmanship(id);
    }

    /**
     * 删除数据
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("工艺删除")
    @DeleteMapping
    @FlowCache(table = "生产工艺", isAdd = false)
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.workmanshipService.deleteWorkmanship(idList);
    }
}

