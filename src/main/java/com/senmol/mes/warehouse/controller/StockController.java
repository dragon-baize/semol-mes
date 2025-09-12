package com.senmol.mes.warehouse.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.service.StockService;
import com.senmol.mes.warehouse.vo.StockVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 库位管理(Stock)表控制层
 *
 * @author makejava
 * @since 2023-01-29 17:08:53
 */
@Validated
@RestController
@RequestMapping("/warehouse/stock")
public class StockController {

    @Resource
    private StockService stockService;
    @Resource
    private RedisService redisService;

    /**
     * 分页、主键查询数据
     *
     * @param page  分页对象
     * @param stock 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<StockEntity> page, StockEntity stock) {
        return SaResult.data(this.stockService.page(page, new QueryWrapper<>(stock)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}/{type}")
    public SaResult selectOne(@PathVariable("id") Long id, @PathVariable("type") Integer type) {
        return SaResult.data(this.stockService.selectOne(id, type));
    }

    /**
     * 新增数据
     *
     * @param stock 实体对象
     * @return 新增结果
     */
    @Logger("库位新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody StockEntity stock) {
        long l = CheckToolUtil.checkCodeExist(this.stockService, null, stock.getCode());
        if (l > 0L) {
            return SaResult.error("库位编号已存在");
        }

        this.stockService.save(stock);
        // 添加到缓存
        this.redisService.set(RedisKeyEnum.WAREHOUSE_STOCK.getKey() + stock.getId(), Convert.convert(StockVo.class,
                stock), RedisKeyEnum.WAREHOUSE_STOCK.getTimeout());
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    /**
     * 修改数据
     *
     * @param stock 实体对象
     * @return 修改结果
     */
    @Logger("库位修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody StockEntity stock) {
        long l = CheckToolUtil.checkCodeExist(this.stockService, stock.getId(), stock.getCode());
        if (l > 0L) {
            return SaResult.error("库位编号已存在");
        }

        this.stockService.updateById(stock);
        // 添加到缓存
        this.redisService.set(RedisKeyEnum.WAREHOUSE_STOCK.getKey() + stock.getId(), Convert.convert(StockVo.class,
                stock), RedisKeyEnum.WAREHOUSE_STOCK.getTimeout());
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("库位删除")
    @DeleteMapping
    public SaResult delete(@RequestParam("idList") Long idList) {
        this.stockService.removeById(idList);
        // 删除缓存
        this.redisService.del(RedisKeyEnum.WAREHOUSE_STOCK.getKey() + idList);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

