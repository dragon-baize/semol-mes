package com.senmol.mes.system.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.entity.PositionEntity;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.service.PositionService;
import com.senmol.mes.system.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 职位(Position)表控制层
 *
 * @author makejava
 * @since 2023-01-29 13:37:41
 */
@RestController
@RequestMapping("/system/position")
public class PositionController {

    @Resource
    private PositionService positionService;
    @Resource
    private UserService userService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        return SaResult.data(this.positionService.lambdaQuery().list());
    }

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param position 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<PositionEntity> page, PositionEntity position) {
        return SaResult.data(this.positionService.page(page, new QueryWrapper<>(position)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.positionService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param position 实体对象
     * @return 新增结果
     */
    @Logger("职位新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody PositionEntity position) {
        this.positionService.save(position);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    /**
     * 修改数据
     *
     * @param position 实体对象
     * @return 修改结果
     */
    @Logger("职位修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody PositionEntity position) {
        this.positionService.updateById(position);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("职位删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        List<UserEntity> list = this.userService.lambdaQuery().eq(UserEntity::getPositionId, idList).list();
        if (CollUtil.isNotEmpty(list)) {
            return SaResult.error("存在绑定该职位的员工数据");
        }

        this.positionService.removeById(idList);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }
}

