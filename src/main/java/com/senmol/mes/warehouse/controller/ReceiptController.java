package com.senmol.mes.warehouse.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.warehouse.entity.ReceiptEntity;
import com.senmol.mes.warehouse.service.ReceiptService;
import com.senmol.mes.warehouse.vo.ReceiptVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 收货管理(Receipt)表控制层
 *
 * @author makejava
 * @since 2023-02-13 11:02:36
 */
@RestController
@RequestMapping("/warehouse/receipt")
public class ReceiptController {

    @Resource
    private ReceiptService receiptService;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param receipt 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(PageUtil<ReceiptVo> page, ReceiptEntity receipt) {
        return this.receiptService.selectAll(page, receipt);
    }

    /**
     * 根据计划编号查询收货数据
     *
     * @param planOrderNo 计划编号
     * @return 收货数据
     */
    @GetMapping("getByPlanOrderNo/{planOrderNo}")
    public SaResult getByPlanOrderNo(@PathVariable("planOrderNo") String planOrderNo) {
        return this.receiptService.getByPlanOrderNo(planOrderNo);
    }

    /**
     * 新增数据
     *
     * @param receipt 实体对象
     * @return 新增结果
     */
    @Logger("收货新增")
    @PostMapping
    public SaResult insert(@RequestBody ReceiptEntity receipt) {
        return this.receiptService.insertReceipt(receipt);
    }

    /**
     * 修改数据
     *
     * @param receipt 实体对象
     * @return 修改结果
     */
    @Logger("收货修改")
    @PutMapping
    public SaResult update(@RequestBody ReceiptEntity receipt) {
        return this.receiptService.updateReceipt(receipt);
    }

    /**
     * 收货删除
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("收货删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.receiptService.delById(id);
    }

}

