package com.senmol.mes.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.page.StorageTotal;
import com.senmol.mes.warehouse.page.Summary;
import com.senmol.mes.warehouse.page.SummaryTotal;
import com.senmol.mes.warehouse.vo.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 入库记录(Storage)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
public interface StorageMapper extends BaseMapper<StorageEntity> {

    /**
     * 查询仓库指定物料数量
     *
     * @param goodIds 物料ID列表
     * @param type    物品类型 0-成品 1-半成品 2-原料 3-非原料
     * @return 指定物料数量
     */
    List<CountVo> getKcl(@Param("goodIds") Set<Long> goodIds, @Param("type") Integer type);

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param storage 查询实体
     * @return 所有数据
     */
    List<StorageVo> selectAll(@Param("page") PageUtil<StorageVo> page, @Param("storage") StorageEntity storage);

    /**
     * 分页查询合计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param storage   查询实体
     * @return 合计
     */
    StorageTotal selectTotal(@Param("startTime") LocalDate startTime,
                             @Param("endTime") LocalDate endTime,
                             @Param("storage") StorageEntity storage);

    /**
     * 分页查询库存大于0、非残次品
     *
     * @param page    分页对象
     * @param storage 查询实体
     * @return 所有数据
     */
    List<StorageVo> getList(Page<StorageVo> page, @Param("storage") StorageEntity storage);

    /**
     * 查询库位物品
     *
     * @param stockId 库位ID
     * @param type    物品类型
     * @return 物品列表
     */
    List<StorageVo> getByStockId(@Param("stockId") Long stockId, @Param("type") Integer type);

    /**
     * 总览
     *
     * @param page       分页对象
     * @param queryParam 查询参数
     * @param type       物品类型
     * @return 所有数据
     */
    List<StorageInfo> overviewData(Page<StorageInfo> page, @Param("qp") String queryParam, @Param("type") Integer type);

    /**
     * 库存汇总表
     *
     * @param ids     存货ID列表
     * @param type    存货类型 2-物料
     * @param endTime 结束时间
     * @return 结果
     */
    List<StorageEntity> summary(@Param("ids") List<Long> ids,
                                @Param("type") Integer type,
                                @Param("endTime") LocalDate endTime);

    /**
     * 本期合计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param summary   查询实体
     * @return 结果
     */
    SummaryTotal getTotal(@Param("startTime") LocalDate startTime,
                          @Param("endTime") LocalDate endTime,
                          @Param("arg") Summary summary);

    /**
     * 期初合计
     *
     * @param type      类型 0-产品 2-物料
     * @param startTime 开始时间
     * @return 结果
     */
    SummaryTotal getPreTotal(@Param("type") Integer type, @Param("startTime") LocalDate startTime);

    /**
     * 出入库明细
     *
     * @param page      分页对象
     * @param inventory 查询实体
     * @return 明细列表
     */
    List<Inventory> inventory(@Param("page") PageUtil<Inventory> page, @Param("inventory") Inventory inventory);

    /**
     * 出入库数量合计
     *
     * @param inventory 查询实体
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 明细列表
     */
    BigDecimal sumInventory(@Param("inventory") Inventory inventory,
                            @Param("startTime") LocalDate startTime,
                            @Param("endTime") LocalDate endTime);

    /**
     * 物品库存明细
     *
     * @param goodsId 物品ID
     * @return 明细
     */
    List<GoodsMx> goodsMx(@Param("goodsId") Long goodsId);

    /**
     * 批量根据ID修改剩余量
     *
     * @param storages   实体列表
     * @param updateTime 修改时间
     * @param updateUser 修改人
     */
    void modifyBatch(@Param("storages") List<StorageEntity> storages,
                     @Param("updateTime") LocalDateTime updateTime,
                     @Param("updateUser") Long updateUser);

    /**
     * 批量根据批次号修改剩余量
     *
     * @param storages 实体列表
     */
    void updateBatchByBatchNo(@Param("storages") List<StorageEntity> storages);

    /**
     * 查询出库单物品
     *
     * @param code 出库单单号
     * @return 物品列表
     */
    List<GoodsInfo> getGoodsByOutBoundCode(@Param("code") String code);

    /**
     * 查询采购单入库数量
     *
     * @param code 采购单单号
     * @return 入库数量
     */
    BigDecimal getQtyByPurchaseCode(@Param("code") String code);

    /**
     * 采购单号查入库记录
     *
     * @param codes   采购单号
     * @param invoice 采购开票ID
     * @return 入库记录
     */
    List<StorageEntity> getByCgDh(@Param("codes") List<String> codes, @Param("invoice") Long invoice);

    /**
     * 获取所有物料/产品信息
     *
     * @return 物料/产品信息列表
     */
    List<ProduceGoods> getGoods();

    /**
     * 物料库存查询
     *
     * @param goodsIds 物料ID列表
     * @return 库存数据
     */
    List<CountVo> getByGoodsIds(@Param("goodsIds") Set<Long> goodsIds);

    /**
     * 查询退货单明细
     *
     * @param receiptCode 采购单号
     * @return 出库单明细
     */
    BigDecimal getReturns(@Param("receiptCode") String receiptCode);

    /**
     * 批量新增
     *
     * @param entities 实体列表
     */
    void insertBatch(@Param("entities") List<StorageEntity> entities);

    /**
     * 批量变更状态
     *
     * @param entities   实体列表
     * @param updateTime 更新时间
     * @param updateUser 更新人
     */
    void updateStatusByIds(@Param("entities") List<StorageEntity> entities,
                           @Param("updateTime") LocalDateTime updateTime,
                           @Param("updateUser") Long updateUser);

    /**
     * 批量设置开票ID
     *
     * @param storageIds 主键列表
     * @param invoice    开票ID
     */
    void setInvoices(@Param("storageIds") List<Long> storageIds, @Param("invoice") Long invoice);

}

