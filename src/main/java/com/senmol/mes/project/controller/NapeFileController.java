package com.senmol.mes.project.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.project.entity.NapeFile;
import com.senmol.mes.project.service.NapeFileService;
import com.senmol.mes.project.vo.NapeFileVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 添加项文件(NapeFile)表控制层
 *
 * @author makejava
 * @since 2023-03-21 09:43:30
 */
@RestController
@RequestMapping("/project/napeFile")
public class NapeFileController {

    @Resource
    private NapeFileService napeFileService;

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param napeFile 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<NapeFile> page, NapeFile napeFile) {
        return this.napeFileService.selectAll(page, napeFile);
    }

    /**
     * 分页查询审核员的审核文件列表
     *
     * @param page     分页对象
     * @param napeFile 实体对象
     * @return 审核文件列表
     */
    @GetMapping("getByUserId")
    public SaResult getByUserId(Page<NapeFileVo> page, NapeFile napeFile) {
        return this.napeFileService.getByUserId(page, napeFile);
    }

    /**
     * 查询审批详情
     *
     * @param id 添加项文件ID
     * @return 审批详情
     */
    @GetMapping("getOneById/{id}")
    public SaResult getOneById(@PathVariable("id") Long id) {
        return this.napeFileService.getOneById(id);
    }

    /**
     * 新增数据
     *
     * @param napeFile 实体对象
     * @return 新增结果
     */
    @Logger("添加项文件新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody NapeFile napeFile) {
        return this.napeFileService.insertNapeFile(napeFile);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("添加项文件删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") List<Long> idList) {
        return this.napeFileService.deleteNapeFile(idList);
    }
}

