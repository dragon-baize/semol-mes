package com.senmol.mes.quality.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.service.StorageReservaService;
import com.senmol.mes.quality.vo.StorageReservaVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 保留品记录(QualityStorageReserva)表控制层
 *
 * @author makejava
 * @since 2023-12-20 09:32:46
 */
@RestController
@RequestMapping("quality/storage/reserva")
public class StorageReservaController {

    @Resource
    private StorageReservaService storageReservaService;

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param storageReserva 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<StorageReservaVo> page, StorageReserva storageReserva) {
        // 主键查询
        if (ObjectUtil.isNotNull(storageReserva.getId())) {
            return this.storageReservaService.selectOne(storageReserva.getId());
        }

        if (ObjectUtil.isNull(storageReserva.getType())) {
            return SaResult.error("缺少类型");
        }

        // 分页
        return SaResult.data(this.storageReservaService.selectAll(page, storageReserva));
    }

    /**
     * 新增数据
     *
     * @param storageReserva 实体对象
     * @return 新增结果
     */
    @Logger("保留品记录新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody StorageReserva storageReserva) {
        storageReserva.setTester(StpUtil.getLoginIdAsLong());
        this.storageReservaService.save(storageReserva);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    /**
     * 批量新增
     *
     * @param reservas 对象列表
     * @return 新增结果
     */
    @Logger("保留品记录新增")
    @PostMapping("batch")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody List<StorageReserva> reservas) {
        return this.storageReservaService.insertBatch(reservas);
    }

    /**
     * 修改数据
     *
     * @param storageReserva 实体对象
     * @return 修改结果
     */
    @Logger("保留品记录修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody StorageReserva storageReserva) {
        return this.storageReservaService.updateReserva(storageReserva);
    }

    /**
     * 批量修改
     *
     * @param storageReservas 对象列表
     * @return 修改结果
     */
    @Logger("保留品记录修改")
    @PutMapping("batch")
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody List<StorageReserva> storageReservas) {
        return this.storageReservaService.modifyBatch(storageReservas);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("保留品记录删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        this.storageReservaService.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

