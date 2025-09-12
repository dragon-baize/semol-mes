package com.senmol.mes.project.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.project.entity.Complaint;
import com.senmol.mes.project.service.ComplaintService;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 客诉管理(Complaint)表控制层
 *
 * @author makejava
 * @since 2023-03-22 15:05:56
 */
@Validated
@RestController
@RequestMapping("/project/complaint")
public class ComplaintController {

    @Resource
    private ComplaintService complaintService;
    @Resource
    private SysFromRedis sysFromRedis;

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据
     *
     * @param page 分页对象
     * @param complaint 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<Complaint> page, Complaint complaint) {
        // 主键查询
        if (ObjectUtil.isNotNull(complaint.getId())) {
            return SaResult.data(this.complaintService.getById(complaint.getId()));
        }

        // 分页
        Page<Complaint> complaintPage = this.complaintService.page(page, new QueryWrapper<>(complaint));
        List<Complaint> records = complaintPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            records.forEach(item -> item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser())));
        }

        return SaResult.data(complaintPage);
    }

    /**
     * 新增数据
     *
     * @param complaint 实体对象
     * @return 新增结果
     */
    @Logger("客诉新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody Complaint complaint) {
        this.complaintService.save(complaint);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    /**
     * 修改数据
     *
     * @param complaint 实体对象
     * @return 修改结果
     */
    @Logger("客诉修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody Complaint complaint) {
        this.complaintService.updateById(complaint);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("客诉删除")
    @DeleteMapping
    public SaResult delete(@NotEmpty(message = "主键列表为空") @RequestParam("idList") List<Long> idList) {
        this.complaintService.removeByIds(idList);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }
}

