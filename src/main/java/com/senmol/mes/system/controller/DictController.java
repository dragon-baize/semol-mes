package com.senmol.mes.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.entity.DictEntity;
import com.senmol.mes.system.service.DictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典(Dict)表控制层
 *
 * @author makejava
 * @since 2023-01-03 17:04:17
 */
@RestController
@RequestMapping("/system/dict")
public class DictController {

    @Resource
    private DictService dictService;

    /**
     * 字典名称查字典明细
     *
     * @param title 字典名称
     * @return 字典明细
     */
    @CrossOrigin
    @GetMapping("getMx/{title}")
    public SaResult getMx(@NotBlank(message = "缺少字典名称") @PathVariable("title") String title) {
        return this.dictService.getMx(title);
    }

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param dict 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<DictEntity> page, DictEntity dict) {
        return SaResult.data(this.dictService.page(page, new QueryWrapper<>(dict)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable Long id) {
        return SaResult.data(this.dictService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param dict 实体对象
     * @return 新增结果
     */
    @Logger("字典新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody DictEntity dict) {
        boolean isExist = this.titleIsExist(dict);
        if (isExist) {
            return SaResult.error("字典名称重复");
        }
        this.dictService.save(dict);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    /**
     * 修改数据
     *
     * @param dict 实体对象
     * @return 修改结果
     */
    @Logger("字典修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody DictEntity dict) {
        boolean isExist = this.titleIsExist(dict);
        if (isExist) {
            return SaResult.error("字典名称重复");
        }
        this.dictService.updateById(dict);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 删除数据
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("字典删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.dictService.deleteDict(idList);
    }

    private boolean titleIsExist(DictEntity dict) {
        List<DictEntity> list = this.dictService.lambdaQuery().eq(DictEntity::getTitle, dict.getTitle()).list();
        if (dict.getId() != null) {
            list = list.stream()
                    .filter(item -> !item.getId().equals(dict.getId()))
                    .collect(Collectors.toList());
        }

        return list.size() > 0;
    }
}

