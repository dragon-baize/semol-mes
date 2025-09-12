package com.senmol.mes.warehouse.controller;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.listener.ExcelListener;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.page.InventoryPage;
import com.senmol.mes.warehouse.page.StoragePage;
import com.senmol.mes.warehouse.page.Summary;
import com.senmol.mes.warehouse.page.SummaryPage;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.vo.Inventory;
import com.senmol.mes.warehouse.vo.StorageInfo;
import com.senmol.mes.warehouse.vo.StorageVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 入库记录(Storage)表控制层
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
@Validated
@RestController
@RequestMapping("warehouse/storage")
public class StorageController {

    @Resource
    private StorageService storageService;

    /**
     * 获取产品库存量
     *
     * @param productId 产品ID
     * @return 结果
     */
    @GetMapping("getStorage/{productId}")
    public SaResult getStorage(@PathVariable("productId") Long productId) {
        return SaResult.data(this.storageService.getStorage(productId));
    }

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据
     *
     * @param page    分页对象
     * @param storage 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(StoragePage page, StorageEntity storage) {
        // 分页
        return SaResult.data(this.storageService.selectAll(page, storage));
    }

    /**
     * 分页查询库存大于0、非残次品
     *
     * @param page    分页对象
     * @param storage 查询实体
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList(Page<StorageVo> page, StorageEntity storage) {
        // 分页
        return SaResult.data(this.storageService.getList(page, storage));
    }

    /**
     * 查询出库单物料库存
     *
     * @param code 出库单编号
     * @return 库存明细
     */
    @GetMapping("byOBCode/{code}")
    public SaResult getGoodsByOutBoundCode(@PathVariable("code") String code) {
        return SaResult.data(this.storageService.getGoodsByOutBoundCode(code));
    }

    /**
     * 查询采购单物料库存
     *
     * @param code 出库单编号
     * @return 库存明细
     */
    @GetMapping("byPHBCode/{code}")
    public SaResult getQtyByPurchaseCode(@PathVariable("code") String code) {
        return SaResult.data(this.storageService.getQtyByPurchaseCode(code));
    }

    /**
     * 采购单号查入库记录
     *
     * @param code    采购单号
     * @param invoice 采购开票ID
     * @return 入库记录
     */
    @GetMapping("getByCgDh/{code}/{invoice}")
    public SaResult getByCgDh(@PathVariable("code") String code, @PathVariable("invoice") Long invoice) {
        return this.storageService.getByCgDh(code, invoice);
    }

    /**
     * 总览
     *
     * @param page       分页对象
     * @param queryParam 查询参数
     * @param type       物品类型
     * @return 所有数据
     */
    @GetMapping("overviewData")
    public SaResult overviewData(Page<StorageInfo> page, String queryParam, Integer type) {
        return SaResult.data(this.storageService.overviewData(page, queryParam, type));
    }

    /**
     * 库存汇总表
     *
     * @param page    分页对象
     * @param summary 查询实体
     * @return 结果
     */
    @GetMapping("summary")
    public SaResult summary(SummaryPage page, Summary summary) {
        return this.storageService.summary(page, summary);
    }

    /**
     * 出入库明细
     *
     * @param page      分页对象
     * @param inventory 查询实体
     * @return 明细列表
     */
    @GetMapping("inventory")
    public SaResult inventory(InventoryPage page, Inventory inventory) {
        return SaResult.data(this.storageService.inventory(page, inventory));
    }

    /**
     * 物品库存明细
     *
     * @param goodsId 物品ID
     * @return 明细
     */
    @GetMapping("/goodsMx/{goodsId}")
    public SaResult goodsMx(@PathVariable("goodsId") Long goodsId) {
        return SaResult.data(this.storageService.goodsMx(goodsId));
    }

    /**
     * 新增数据
     *
     * @param storage 实体对象
     * @return 新增结果
     */
    @Logger("入库")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody StorageVo storage) {
        return this.storageService.insertStorage(storage);
    }

    /**
     * 导入
     */
    @Logger("入库导入")
    @PostMapping("import")
    public SaResult importExcel(MultipartFile file) {
        String result = CheckToolUtil.checkFileFormat(file);
        if (result != null) {
            return SaResult.error(result);
        }
        // ExcelListener 不能被spring管理，要每次读取excel都要new，然后里面用到spring可以构造方法传进去
        ExcelListener listener = new ExcelListener(this.storageService);
        try {
            // 这里需要指定读用哪个class去读，然后读取第一个sheet文件流会自动关闭
            EasyExcel.read(file.getInputStream(), StorageVo.class, listener).sheet().autoTrim(true).doRead();
            // 返回导入失败的数据
            return new SaResult(200, "导入成功", listener.getFailList());
        } catch (IOException e) {
            e.printStackTrace();
            return SaResult.error("导入失败，请检查模版是否正确");
        }
    }

}

