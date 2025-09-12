package com.senmol.mes.project.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.project.entity.Manage;
import com.senmol.mes.project.service.ManageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 项目管理(Manage)表控制层
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
@Validated
@RestController
@RequestMapping("/project/manage")
public class ManageController {

    @Resource
    private ManageService manageService;

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据，size=-1时不分页显示所有数据
     *
     * @param page   分页对象
     * @param manage 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<Manage> page, Manage manage) {
        // 主键查询
        if (ObjectUtil.isNotNull(manage.getId())) {
            return SaResult.data(this.manageService.selectOne(manage.getId()));
        }

        // 查询所有
        if (page.getSize() == -1) {
            List<Manage> list = this.manageService.lambdaQuery(manage).list();
            return SaResult.data(list);
        }

        // 分页
        return SaResult.data(this.manageService.selectAll(page, manage));
    }

    /**
     * 分页查询审核员的审核项目列表
     *
     * @param page   分页对象
     * @param manage 实体对象
     * @return 审批项目列表
     */
    @GetMapping("getByUserId")
    public SaResult getByUserId(Page<Manage> page, Manage manage) {
        return SaResult.data(this.manageService.getByUserId(page, manage));
    }

    /**
     * 主键查询审核信息
     *
     * @param id 主键
     * @return 审核信息
     */
    @GetMapping("getOneById/{id}")
    public SaResult getOneById(@PathVariable("id") Long id) {
        return SaResult.data(this.manageService.getOneById(id));
    }

    /**
     * 新增数据
     *
     * @param manage 实体对象
     * @return 新增结果
     */
    @Logger("项目新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody Manage manage) {
        return this.manageService.insertManage(manage);
    }

    /**
     * 修改数据
     *
     * @param manage 实体对象
     * @return 修改结果
     */
    @Logger("项目修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody Manage manage) {
        return this.manageService.updateManage(manage);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("项目删除")
    @DeleteMapping
    public SaResult delete(@NotEmpty(message = "主键列表为空") @RequestParam("idList") List<Long> idList) {
        return this.manageService.deleteManage(idList);
    }

}

