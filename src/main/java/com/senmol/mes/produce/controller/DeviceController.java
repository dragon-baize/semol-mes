package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.produce.entity.DeviceEntity;
import com.senmol.mes.produce.service.DeviceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设备管理(Device)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:00:13
 */
@RestController
@RequestMapping("/produce/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    /**
     * 查询所有数据
     *
     * @param productLineId 产线ID
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList(Long productLineId, Long stationId) {
        if (productLineId == null && stationId == null) {
            return SaResult.data(this.deviceService.list());
        } else {
            return SaResult.data(this.deviceService.getList(productLineId, stationId));
        }
    }

    /**
     * 分页查询所有数据
     *
     * @param page   分页对象
     * @param device 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<DeviceEntity> page, DeviceEntity device) {
        return this.deviceService.selectAll(page, device);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.deviceService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param device 实体对象
     * @return 新增结果
     */
    @Logger("设备新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody DeviceEntity device) {
        return this.deviceService.insertDevice(device);
    }

    /**
     * 修改数据
     *
     * @param device 实体对象
     * @return 修改结果
     */
    @Logger("设备修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody DeviceEntity device) {
        return this.deviceService.updateDevice(device);
    }

    /**
     * 删除数据
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("设备删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.deviceService.deleteDevice(idList);
    }

}

