package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.service.OutboundService;
import com.senmol.mes.plan.vo.OutboundInfo;
import com.senmol.mes.plan.vo.OutboundVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 出库单(Outbound)表控制层
 *
 * @author makejava
 * @since 2023-03-13 10:21:08
 */
@Validated
@RestController
@RequestMapping("/plan/outbound")
public class OutboundController {

    @Resource
    private OutboundService outboundService;

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param outbound 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<OutboundVo> page, OutboundEntity outbound) {
        return this.outboundService.selectAll(page, outbound);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable("id") Long id) {
        return this.outboundService.selectOne(id);
    }

    /**
     * 查询出库单明细
     *
     * @param id 出库单ID
     * @return 明细列表
     */
    @GetMapping("mx/{id}")
    public SaResult mx(@PathVariable("id") Long id) {
        return this.outboundService.mx(id);
    }

    /**
     * 通过单号查询单条数据
     *
     * @param code 单号
     * @return 单条数据
     */
    @GetMapping("getByCode/{code}")
    public SaResult getByCode(@PathVariable("code") String code) {
        return this.outboundService.getByCode(code);
    }

    /**
     * 新增数据
     *
     * @param outbound 实体对象
     * @return 新增结果
     */
    @Logger("出库单新增")
    @PostMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.OutboundEntity", table = "出库单")
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody OutboundVo outbound) {
        return this.outboundService.insertOutbound(outbound);
    }

    /**
     * 新增发货单
     *
     * @param outbound 实体对象
     * @return 新增结果
     */
    @Logger("出库单创建发货单")
    @PostMapping("invoice")
    public SaResult insertInvoice(@Validated(ParamsValidate.Insert.class) @RequestBody OutboundInfo outbound) {
        return this.outboundService.insertInvoice(outbound);
    }

    /**
     * 修改数据
     *
     * @param outboundVo 实体对象
     * @return 修改结果
     */
    @Logger("出库单修改")
    @PutMapping
    @FlowCache(entity = "com.senmol.mes.plan.entity.OutboundEntity", table = "出库单")
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody OutboundVo outboundVo) {
        return this.outboundService.updateOutbound(outboundVo);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Logger("出库单删除")
    @DeleteMapping
    @FlowCache(table = "出库单", isAdd = false)
    public SaResult delete(@NotEmpty(message = "主键列表为空") @RequestParam("idList") List<Long> idList) {
        return this.outboundService.deleteOutbound(idList);
    }

}

