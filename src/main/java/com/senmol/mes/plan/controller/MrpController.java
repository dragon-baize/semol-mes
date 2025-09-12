package com.senmol.mes.plan.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.plan.entity.MrpEntity;
import com.senmol.mes.plan.service.MrpService;
import com.senmol.mes.plan.vo.MrpInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * MRP(Mrp)表控制层
 *
 * @author makejava
 * @since 2023-07-15 11:18:11
 */
@Validated
@RestController
@RequestMapping("/plan/mrp")
public class MrpController {

    @Resource
    private MrpService mrpService;

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据，size=-1时不分页显示所有数据
     *
     * @param page 分页对象
     * @param mrp  查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<MrpEntity> page, MrpEntity mrp) {
        // 主键查询
        if (ObjectUtil.isNotNull(mrp.getId())) {
            return SaResult.data(this.mrpService.selectOne(mrp.getId()));
        }

        // 查询所有
        if (page.getSize() == -1) {
            List<MrpEntity> list = this.mrpService.lambdaQuery(mrp).list();
            return SaResult.data(list);
        }

        // 分页
        return SaResult.data(this.mrpService.selectAll(page, mrp));
    }

    /**
     * 产品销售未完成订单量，去除本销售订单的
     *
     * @param productIds 产品ID列表
     * @return 数量
     */
    @GetMapping("unfinished")
    public SaResult unfinishedOrder(@RequestParam("productIds") List<Long> productIds,
                                    @RequestParam("saleOrderId") Long saleOrderId) {
        return this.mrpService.unfinishedOrder(productIds, saleOrderId);
    }

    /**
     * 物料销售未完成订单量
     *
     * @param productId 产品ID
     * @return 数量
     */
    @GetMapping("materialQty/{productId}")
    public SaResult materialQty(@PathVariable("productId") Long productId) {
        return this.mrpService.materialQty(productId);
    }

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @Logger("MRP新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody MrpInfo info) {
        return this.mrpService.insertMrp(info);
    }

    /**
     * 修改数据
     *
     * @param mrp 实体对象
     * @return 修改结果
     */
    @Logger("MRP修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody MrpEntity mrp) {
        this.mrpService.updateById(mrp);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("MRP删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.mrpService.deleteMrp(id);
    }

    /**
     * 物料销售未完成订单量
     */
    @GetMapping("unSaleOrder")
    public SaResult unSaleOrder() {
        return this.mrpService.unSaleOrder();
    }

    /**
     * 物料在制量
     */
    @GetMapping("unWorkOrder")
    public SaResult unWorkOrder() {
        return this.mrpService.unWorkOrder();
    }
}

