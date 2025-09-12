package com.senmol.mes.warehouse.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.page.InventoryPage;
import com.senmol.mes.warehouse.page.StoragePage;
import com.senmol.mes.warehouse.page.Summary;
import com.senmol.mes.warehouse.page.SummaryPage;
import com.senmol.mes.warehouse.vo.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 入库记录(Storage)表服务接口
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
public interface StorageService extends IService<StorageEntity> {

    /**
     * 获取产品库存量
     *
     * @param productId 产品ID
     * @return 结果
     */
    BigDecimal getStorage(Long productId);

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据
     *
     * @param page    分页对象
     * @param storage 查询实体
     * @return 所有数据
     */
    Page<StorageVo> selectAll(StoragePage page, StorageEntity storage);

    /**
     * 查询仓库指定物料数量
     *
     * @param goodIds 物料ID列表
     * @param type    物品类型 0-成品 1-半成品 2-原料 3-非原料
     * @return 指定物料数量
     */
    List<CountVo> getKcl(Set<Long> goodIds, Integer type);

    /**
     * 分页查询库存大于0、非残次品
     *
     * @param page    分页对象
     * @param storage 查询实体
     * @return 所有数据
     */
    Page<StorageVo> getList(Page<StorageVo> page, StorageEntity storage);

    /**
     * 查询库位物品
     *
     * @param stockId 库位ID
     * @param type    物品类型
     * @return 物品列表
     */
    List<StorageVo> getByStockId(Long stockId, Integer type);

    /**
     * 总览
     *
     * @param page       分页对象
     * @param queryParam 查询参数
     * @param type       物品类型
     * @return 所有数据
     */
    Page<StorageInfo> overviewData(Page<StorageInfo> page, String queryParam, Integer type);

    /**
     * 库存汇总表
     *
     * @param page    分页对象
     * @param summary 查询实体
     * @return 结果
     */
    SaResult summary(SummaryPage page, Summary summary);

    /**
     * 出入库明细
     *
     * @param page      分页对象
     * @param inventory 查询实体
     * @return 明细列表
     */
    InventoryPage inventory(InventoryPage page, Inventory inventory);

    /**
     * 物品库存明细
     *
     * @param goodsId 物品ID
     * @return 明细
     */
    List<GoodsMx> goodsMx(Long goodsId);

    /**
     * 新增数据
     *
     * @param storage 实体对象
     * @return 新增结果
     */
    SaResult insertStorage(StorageVo storage);

    /**
     * 批量根据ID修改剩余量
     *
     * @param storages   实体列表
     * @param updateTime 修改时间
     * @param userId     修改人
     */
    void modifyBatch(List<StorageEntity> storages, LocalDateTime updateTime, Long userId);

    /**
     * 批量根据批次号修改剩余量
     *
     * @param storages 实体列表
     */
    void updateBatchByBatchNo(List<StorageEntity> storages);

    /**
     * 查询出库单物品
     *
     * @param code 出库单单号
     * @return 物品列表
     */
    List<GoodsInfo> getGoodsByOutBoundCode(String code);

    /**
     * 查询采购单入库数量
     *
     * @param code 采购单单号
     * @return 入库数量
     */
    BigDecimal getQtyByPurchaseCode(String code);

    /**
     * 采购单号查入库记录
     *
     * @param code    采购单号
     * @param invoice 采购开票ID
     * @return 入库记录
     */
    SaResult getByCgDh(String code, Long invoice);

    /**
     * 物料库存查询
     *
     * @param goodsIds 物料ID列表
     * @return 库存数据
     */
    List<CountVo> getByGoodsIds(Set<Long> goodsIds);

    /**
     * 查询退货单明细
     *
     * @param receiptCode 采购单号
     * @return 出库单明细
     */
    BigDecimal getReturns(String receiptCode);

    /**
     * excel导入
     *
     * @param cachedDataList 导入数据
     * @return 导入结果
     */
    List<Object> insertByExcel(List<Object> cachedDataList);

    /**
     * 批量新增
     *
     * @param entities 实体列表
     */
    void insertBatch(List<StorageEntity> entities);

    /**
     * 批量变更状态
     *
     * @param storages   实体列表
     * @param updateTime 更新时间
     * @param updateUser 更新人
     */
    void updateStatusByIds(List<StorageEntity> storages, LocalDateTime updateTime, Long updateUser);

    /**
     * 批量设置开票ID
     *
     * @param storageIds 主键列表
     * @param invoice    开票ID
     */
    void setInvoices(List<Long> storageIds, Long invoice);

}

