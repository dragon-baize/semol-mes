package com.senmol.mes.project.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.project.entity.QualityManage;
import com.senmol.mes.project.service.QualityManageService;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 质量管理(QualityManage)表控制层
 *
 * @author makejava
 * @since 2023-03-22 14:31:19
 */
@Validated
@RestController
@RequestMapping("/project/quality/manage")
public class QualityManageController {

    @Resource
    private QualityManageService qualityManageService;
    @Resource
    private SysFromRedis sysFromRedis;

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据
     *
     * @param page 分页对象
     * @param qualityManage 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<QualityManage> page, QualityManage qualityManage) {
        // 主键查询
        if (ObjectUtil.isNotNull(qualityManage.getId())) {
            return SaResult.data(this.qualityManageService.getById(qualityManage.getId()));
        }

        // 分页
        Page<QualityManage> managePage = this.qualityManageService.page(page, new QueryWrapper<>(qualityManage));
        List<QualityManage> records = managePage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            records.forEach(item -> item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser())));
        }

        return SaResult.data(managePage);
    }

    /**
     * 新增数据
     *
     * @param qualityManage 实体对象
     * @return 新增结果
     */
    @Logger("质量新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody QualityManage qualityManage) {
        this.qualityManageService.save(qualityManage);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    /**
     * 修改数据
     *
     * @param qualityManage 实体对象
     * @return 修改结果
     */
    @Logger("质量修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody QualityManage qualityManage) {
        this.qualityManageService.updateById(qualityManage);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("质量删除")
    @DeleteMapping
    public SaResult delete(@NotEmpty(message = "主键列表为空") @RequestParam("idList") List<Long> idList) {
        this.qualityManageService.removeByIds(idList);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }
}

