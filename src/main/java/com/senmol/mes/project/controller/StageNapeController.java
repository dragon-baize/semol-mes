package com.senmol.mes.project.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.project.entity.StageNape;
import com.senmol.mes.project.service.StageNapeService;
import com.senmol.mes.project.vo.StageNapeVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 添加项(StageNape)表控制层
 *
 * @author makejava
 * @since 2023-03-21 09:27:23
 */
@Validated
@RestController
@RequestMapping("/project/stageNape")
public class StageNapeController {

    @Resource
    private StageNapeService stageNapeService;

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据，size=-1时不分页显示所有数据
     *
     * @param page      分页对象
     * @param stageNape 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<StageNape> page, StageNape stageNape) {
        // 主键查询
        if (ObjectUtil.isNotNull(stageNape.getId())) {
            return SaResult.data(this.stageNapeService.getOneById(stageNape.getId()));
        }

        // 查询所有
        if (page.getSize() == -1) {
            List<StageNape> list = this.stageNapeService.lambdaQuery(stageNape).list();
            return SaResult.data(Convert.toList(StageNapeVo.class, list));
        }

        // 分页
        return SaResult.data(this.stageNapeService.selectAll(page, stageNape));
    }

    /**
     * 分页查询审核员的审核添加项列表
     *
     * @param page      分页对象
     * @param stageNape 实体对象
     * @return 审批项目列表
     */
    @GetMapping("getByUserId")
    public SaResult getByUserId(Page<StageNape> page, StageNape stageNape) {
        return SaResult.data(this.stageNapeService.getByUserId(page, stageNape));
    }

    /**
     * 查询项目添加项树
     *
     * @param manageId 项目ID
     * @return 添加项树
     */
    @GetMapping("getByManageId/{manageId}")
    public SaResult getByManageId(@PathVariable("manageId") Long manageId) {
        return this.stageNapeService.getByManageId(manageId);
    }

    /**
     * 新增数据
     *
     * @param stageNape 实体对象
     * @return 新增结果
     */
    @Logger("添加项新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody StageNape stageNape) {
        return this.stageNapeService.insertStageNape(stageNape);
    }

    /**
     * 修改数据
     *
     * @param stageNape 实体对象
     * @return 修改结果
     */
    @Logger("添加项修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody StageNape stageNape) {
        return this.stageNapeService.updateStageNape(stageNape);
    }

    /**
     * 删除数据(已审核的不能删)
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("添加项删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.stageNapeService.deleteStageNape(id);
    }
}

