package com.senmol.mes.warehouse.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.warehouse.entity.RetrievalEntity;
import com.senmol.mes.warehouse.service.RetrievalService;
import com.senmol.mes.warehouse.vo.RetrievalInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 出库记录(Retrieval)表控制层
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
@Validated
@RestController
@RequestMapping("warehouse/retrieval")
public class RetrievalController {

    @Resource
    private RetrievalService retrievalService;

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据，size=-1时不分页显示所有数据
     *
     * @param page      分页对象
     * @param retrieval 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<RetrievalEntity> page, RetrievalEntity retrieval) {
        // 主键查询
        if (ObjectUtil.isNotNull(retrieval.getId())) {
            return SaResult.data(this.retrievalService.selectOne(retrieval.getId(), retrieval.getType()));
        }

        // 分页
        return SaResult.data(this.retrievalService.selectAll(page, retrieval));
    }

    /**
     * 批量新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @Logger("出库")
    @PostMapping
    public SaResult insert(@RequestBody RetrievalInfo info) {
        return this.retrievalService.insertRetrievalInfo(info);
    }

}

