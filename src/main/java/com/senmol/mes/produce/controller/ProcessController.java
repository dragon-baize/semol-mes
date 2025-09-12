package com.senmol.mes.produce.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.produce.service.ProcessService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 工序管理(Process)表控制层
 *
 * @author makejava
 * @since 2023-01-29 15:00:52
 */
@Validated
@RestController
@RequestMapping("/produce/process")
public class ProcessController {

    @Resource
    private ProcessService processService;

    /**
     * 查询所有数据
     *
     * @param workmanshipId 工艺ID
     * @param wmsVersion    工艺版本号
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList(@RequestParam("workmanshipId") Long workmanshipId,
                            @RequestParam("wmsVersion") Integer wmsVersion) {
        return SaResult.data(this.processService.getUnboundList(workmanshipId, wmsVersion));
    }

    /**
     * 查询产品工序
     */
    @GetMapping("byProductId/{productId}")
    public SaResult byProductId(@PathVariable("productId") Long productId) {
        return SaResult.data(this.processService.byProductId(productId));
    }

}

