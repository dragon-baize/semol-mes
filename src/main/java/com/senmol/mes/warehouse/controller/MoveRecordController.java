package com.senmol.mes.warehouse.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.warehouse.entity.MoveRecord;
import com.senmol.mes.warehouse.service.MoveRecordService;
import com.senmol.mes.warehouse.vo.MoveVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 迁库记录(MoveRecord)表控制层
 *
 * @author makejava
 * @since 2023-12-21 11:24:51
 */
@RestController
@RequestMapping("warehouse/move/record")
public class MoveRecordController {

    @Resource
    private MoveRecordService moveRecordService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param moveRecord 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<MoveRecord> page, MoveRecord moveRecord) {
        // 主键查询
        if (ObjectUtil.isNotNull(moveRecord.getId())) {
            return SaResult.data(this.moveRecordService.getById(moveRecord.getId()));
        }

        // 分页
        return SaResult.data(this.moveRecordService.page(page, new QueryWrapper<>(moveRecord)));
    }

    /**
     * 新增数据
     *
     * @param moveVos 迁移实体
     * @return 新增结果
     */
    @Logger("物品调拨")
    @PostMapping
    public SaResult move(@RequestBody List<MoveVo> moveVos) {
        return this.moveRecordService.move(moveVos);
    }

}

