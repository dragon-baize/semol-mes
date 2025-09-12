package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.produce.entity.StationEntity;
import com.senmol.mes.produce.service.StationService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.system.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 工位管理(Station)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:01:39
 */
@Validated
@RestController
@RequestMapping("/produce/station")
public class StationController {

    @Resource
    private UserService userService;
    @Resource
    private StationService stationService;
    @Resource
    private ProFromRedis proFromRedis;

    /**
     * 获取未绑定工位的用户列表
     *
     * @return 未绑定工位的用户列表
     */
    @GetMapping("getUnboundUser")
    public SaResult getUnboundUser() {
        return SaResult.data(this.userService.list());
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList() {
        return SaResult.data(this.proFromRedis.getStationList());
    }

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param station 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<StationEntity> page, StationEntity station) {
        return this.stationService.selectAll(page, station);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return SaResult.data(this.stationService.selectOne(id));
    }

    /**
     * 查询用户绑定工位、工序
     *
     * @param productLineId 用户ID
     * @return 工位、工序信息
     */
    @GetMapping("getByUserId/{productLineId}")
    public SaResult getByUserId(@PathVariable("productLineId") Long productLineId) {
        return this.stationService.getByUserId(productLineId);
    }

    /**
     * 查询工位不良模式
     *
     * @param id 工位ID
     * @return 不良模式列表
     */
    @GetMapping("getBadMode/{id}")
    public SaResult getBadMode(@PathVariable("id") Long id) {
        return SaResult.data(this.stationService.getBadMode(id));
    }

    /**
     * 新增数据
     *
     * @param station 实体对象
     * @return 新增结果
     */
    @Logger("工位新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody StationEntity station) {
        return this.stationService.insertStation(station);
    }

    /**
     * 修改数据
     *
     * @param station 实体对象
     * @return 修改结果
     */
    @Logger("工位修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody StationEntity station) {
        return this.stationService.updateStation(station);
    }

    /**
     * 删除数据
     *
     * @param idList 主键
     * @return 删除结果
     */
    @Logger("工位删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        return this.stationService.deleteStation(idList);
    }
}

