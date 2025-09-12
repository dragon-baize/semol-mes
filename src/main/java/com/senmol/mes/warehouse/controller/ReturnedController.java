package com.senmol.mes.warehouse.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.warehouse.entity.ReturnedEntity;
import com.senmol.mes.warehouse.service.ReturnedService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 退库记录(Returned)表控制层
 *
 * @author makejava
 * @since 2023-08-04 20:14:40
 */
@Validated
@RestController
@RequestMapping("warehouse/returned")
public class ReturnedController {

    @Resource
    private ReturnedService returnedService;

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param returned 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<ReturnedEntity> page, ReturnedEntity returned) {
        return SaResult.data(this.returnedService.selectAll(page, returned));
    }

    /**
     * 物料唯一码查询物料数据
     */
    @GetMapping("qrCode/{qrCode}")
    public SaResult byQrCode(@PathVariable("qrCode") String qrCode) {
        return this.returnedService.byQrCode(qrCode);
    }

    /**
     * 新增数据
     *
     * @param returnedList 实体对象列表
     * @return 新增结果
     */
    @Logger("退库")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody List<ReturnedEntity> returnedList) {
        return this.returnedService.insertReturnedList(returnedList);
    }

}

