package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.listener.ExcelListener;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.produce.entity.MaterialEntity;
import com.senmol.mes.produce.service.MaterialService;
import com.senmol.mes.produce.vo.MaterialVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 物料管理(Material)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:00:13
 */
@Slf4j
@RestController
@RequestMapping("/produce/material")
public class MaterialController {

    @Resource
    private MaterialService materialService;

    /**
     * 查询所有数据
     *
     * @param supplierId 供应商ID
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList(Long supplierId, Integer type) {
        return SaResult.data(this.materialService.getList(supplierId, type));
    }

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param material 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<MaterialEntity> page, MaterialEntity material) {
        return this.materialService.selectAll(page, material);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return this.materialService.selectOne(id);
    }

    /**
     * 新增数据
     *
     * @param material 实体对象
     * @return 新增结果
     */
    @Logger("物料新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody MaterialEntity material) {
        return this.materialService.insertMaterial(material);
    }

    /**
     * 物料Excel
     *
     * @param file Excel
     * @return 导入结果
     */
    @Logger("物料导入")
    @PostMapping("import")
    public SaResult importExcel(MultipartFile file) {
        String result = CheckToolUtil.checkFileFormat(file);
        if (result != null) {
            return SaResult.error(result);
        }
        // ExcelListener 不能被spring管理，要每次读取excel都要new，然后里面用到spring可以构造方法传进去
        ExcelListener listener = new ExcelListener(this.materialService);
        try {
            // 这里需要指定读用哪个class去读，然后读取第一个sheet文件流会自动关闭
            EasyExcel.read(file.getInputStream(), MaterialVo.class, listener).sheet().autoTrim(true).doRead();
            // 返回导入失败的数据
            return new SaResult(200, "导入成功", listener.getFailList());
        } catch (IOException e) {
            log.error("导入失败, 请检查模版是否正确", e);
            return SaResult.error("导入失败，请检查模版是否正确");
        }
    }

    /**
     * 修改数据
     *
     * @param material 实体对象
     * @return 修改结果
     */
    @Logger("物料修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody MaterialEntity material) {
        return this.materialService.updateMaterial(material);
    }

    /**
     * 删除数据
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("物料删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.materialService.delMaterial(idList);
    }

}

