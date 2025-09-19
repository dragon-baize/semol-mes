package com.senmol.mes.warehouse.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.*;
import com.senmol.mes.plan.service.*;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.produce.service.ProductLineService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.BomVo;
import com.senmol.mes.quality.entity.StorageInspectEntity;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.service.StorageInspectService;
import com.senmol.mes.quality.service.StorageReservaService;
import com.senmol.mes.warehouse.entity.RetrievalEntity;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.service.ReceiptService;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.warehouse.service.StockService;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.vo.MoveVo;
import com.senmol.mes.warehouse.vo.StockVo;
import com.senmol.mes.workorder.entity.WorkOrderFeedEntity;
import com.senmol.mes.workorder.entity.WorkOrderMxEntity;
import com.senmol.mes.workorder.entity.WorkOrderMxProcess;
import com.senmol.mes.workorder.service.WorkOrderFeedService;
import com.senmol.mes.workorder.service.WorkOrderMaterialService;
import com.senmol.mes.workorder.service.WorkOrderMxService;
import com.senmol.mes.workorder.vo.WorkOrderPojo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Component
public class WarAsyncUtil {

    @Resource
    private StockService stockService;
    @Resource
    private StorageInspectService storageInspectService;
    @Resource
    private ReceiptService receiptService;
    @Resource
    private ProduceService produceService;
    @Resource
    private PurchaseOrderService purchaseOrderService;
    @Resource
    private OutsourceService outsourceService;
    @Resource
    private OutboundService outboundService;
    @Resource
    private OutboundMxService outboundMxService;
    @Resource
    private StorageService storageService;
    @Resource
    private WorkOrderMaterialService workOrderMaterialService;
    @Resource
    private WorkOrderFeedService workOrderFeedService;
    @Resource
    private RetrievalMxService retrievalMxService;
    @Resource
    private RedisService redisService;
    @Resource
    private ProductLineService productLineService;
    @Resource
    private WorkOrderMxService workOrderMxService;
    @Resource
    private PurchaseReturnsService purchaseReturnsService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private StorageReservaService storageReservaService;
    @Resource
    private SaleOrderService saleOrderService;

    /**
     * 获取库位缓存
     */
    public StockVo getStock(Long id) {
        if (ObjUtil.isNull(id)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.WAREHOUSE_STOCK.getKey() + id);
        if (BeanUtil.isEmpty(object)) {
            StockEntity stock = this.stockService.getById(id);
            if (ObjUtil.isNull(stock)) {
                return null;
            }

            object = Convert.convert(StockVo.class, stock);
        }

        this.redisService.set(RedisKeyEnum.WAREHOUSE_STOCK.getKey() + id, object, RedisKeyEnum.WAREHOUSE_STOCK.getTimeout());

        return (StockVo) object;
    }

    /**
     * 更新库存量
     */
    @Async
    public CompletableFuture<Void> updateStock(StorageEntity storage) {
        StockEntity stock = this.stockService.getById(storage.getStockId());
        BigDecimal add = stock.getQty().add(storage.getQty());
        this.stockService.lambdaUpdate()
                .set(StockEntity::getQty, add)
                .set(StockEntity::getUpdateTime, storage.getCreateTime())
                .set(StockEntity::getUpdateUser, storage.getCreateUser())
                .eq(StockEntity::getId, stock.getId())
                .update();

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 修改质检状态为入库
     */
    @Async
    public CompletableFuture<Void> updateStorageInspect(StorageEntity storage) {
        this.storageInspectService.lambdaUpdate()
                .set(StorageInspectEntity::getStatus, 2)
                .set(StorageInspectEntity::getUpdateTime, storage.getCreateTime())
                .set(StorageInspectEntity::getUpdateUser, storage.getCreateUser())
                .eq(StorageInspectEntity::getCode, storage.getSiCode())
                .update();

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 更新收货入库数量
     */
    @Async
    public CompletableFuture<Void> updateReceipt(StorageEntity storage) {
        this.receiptService.updateStorageQty(storage.getSiCode(), storage.getQty(), storage.getCreateTime(),
                storage.getCreateUser());
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 更新工单
     */
    @Async
    @Transactional(rollbackFor = BusinessException.class)
    public CompletableFuture<Void> updateWorkOrder(StorageEntity storage) {
        WorkOrderPojo workOrder = this.produceService.getByMxCode(storage.getBatchNo());
        if (ObjectUtil.isNull(workOrder)) {
            return CompletableFuture.completedFuture(null);
        }

        LocalDateTime updateTime = storage.getCreateTime();
        Long updateUser = storage.getCreateUser();
        BigDecimal add = workOrder.getRecQty().add(storage.getQty());
        int i = add.compareTo(workOrder.getProductQty());

        if (i >= 0) {
            // 设置计划的实际完成时间
            this.produceService.lambdaUpdate().set(ProduceEntity::getRealityFinishTime, LocalDateTime.now())
                    .set(ProduceEntity::getRecQty, add).set(ProduceEntity::getStatus, 2)
                    .set(ProduceEntity::getUpdateTime, updateTime).set(ProduceEntity::getUpdateUser, updateUser)
                    .eq(ProduceEntity::getId, workOrder.getId()).update();

            // 设置备货订单为完成
            this.saleOrderService.lambdaUpdate().set(SaleOrderEntity::getStatus, 2)
                    .eq(SaleOrderEntity::getCode, workOrder.getOrderNo()).eq(SaleOrderEntity::getType, 1).update();
        } else {
            // 设置计划的入库数量
            this.produceService.lambdaUpdate().set(ProduceEntity::getRecQty, add)
                    .set(ProduceEntity::getUpdateTime, updateTime).set(ProduceEntity::getUpdateUser, updateUser)
                    .eq(ProduceEntity::getId, workOrder.getId()).update();
        }

        BomVo bom = this.proFromRedis.getBom(storage.getGoodsId());
        ProductLineEntity productLine = this.productLineService.getById(bom.getProductLineId());
        // 变更产线完成率、总量
        BigDecimal total = productLine.getTotal();
        BigDecimal rate = productLine.getRate();
        BigDecimal subtract = total.subtract(workOrder.getQty());
        if (subtract.compareTo(BigDecimal.ZERO) <= 0) {
            subtract = BigDecimal.ZERO;
            rate = BigDecimal.ZERO;
        } else {
            rate = rate.multiply(total).subtract(workOrder.getNonDefective()).divide(subtract, 4, RoundingMode.HALF_UP);
            if (rate.compareTo(BigDecimal.ZERO) < 0) {
                rate = BigDecimal.ZERO;
            }
        }

        this.productLineService.lambdaUpdate().set(ProductLineEntity::getTotal, subtract)
                .set(ProductLineEntity::getRate, rate).eq(ProductLineEntity::getId, productLine.getId()).update();

        this.workOrderMxService.lambdaUpdate().set(WorkOrderMxEntity::getFinish, 2)
                .eq(WorkOrderMxEntity::getCode, storage.getBatchNo()).update();

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 更新采购单
     */
    @Async
    public CompletableFuture<Void> updatePurchaseOrder(StorageEntity storage) {
        PurchaseOrderEntity purchaseOrder = this.purchaseOrderService.getBySiCode(storage.getSiCode());
        if (BeanUtil.isEmpty(purchaseOrder)) {
            return CompletableFuture.completedFuture(null);
        }

        BigDecimal add = purchaseOrder.getStorageQty().add(storage.getQty());
        int status = 3;
        if (purchaseOrder.getConfirmQty().compareTo(add) <= 0) {
            status = 2;
        }

        this.purchaseOrderService.lambdaUpdate()
                .set(PurchaseOrderEntity::getStorageQty, add)
                .set(PurchaseOrderEntity::getStatus, status)
                .set(PurchaseOrderEntity::getUpdateTime, storage.getCreateTime())
                .set(PurchaseOrderEntity::getUpdateUser, storage.getCreateUser())
                .eq(PurchaseOrderEntity::getId, purchaseOrder.getId())
                .update();

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 修改委外计划状态
     */
    @Async
    public CompletableFuture<Void> updateOutsource(StorageEntity storage) {
        OutsourceEntity outsource = this.outsourceService.getBySiCode(storage.getSiCode());
        if (BeanUtil.isEmpty(outsource)) {
            return CompletableFuture.completedFuture(null);
        }

        BigDecimal add = outsource.getStorageQty().add(storage.getQty());
        int status = 1;
        if (outsource.getQty().compareTo(add) <= 0) {
            status = 3;
        }

        this.outsourceService.lambdaUpdate()
                .set(OutsourceEntity::getStorageQty, add)
                .set(OutsourceEntity::getStatus, status)
                .set(OutsourceEntity::getUpdateTime, storage.getCreateTime())
                .set(OutsourceEntity::getUpdateUser, storage.getCreateUser())
                .eq(OutsourceEntity::getId, outsource.getId())
                .update();

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> changeStock(List<RetrievalMxEntity> mxs, RetrievalEntity retrieval) {
        List<StockEntity> stocks = new ArrayList<>(mxs.size());

        for (RetrievalMxEntity mx : mxs) {
            StockEntity stock = new StockEntity();
            stock.setId(mx.getStockId());
            stock.setQty(mx.getQty());
            stocks.add(stock);
        }

        // 批量修改库位数据
        this.stockService.subModifyBatch(stocks, retrieval.getCreateTime(), retrieval.getCreateUser());
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> changeStorage(List<RetrievalMxEntity> mxs, RetrievalEntity retrieval) {
        List<StorageEntity> storages = new ArrayList<>(mxs.size());
        for (RetrievalMxEntity mx : mxs) {
            StorageEntity storage = new StorageEntity();
            storage.setId(mx.getStorageId());
            storage.setResidueQty(mx.getQty());
            storages.add(storage);
        }

        // 批量修改入库数据
        this.storageService.modifyBatch(storages, retrieval.getCreateTime(), retrieval.getCreateUser());
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 修改出库单状态
     */
    @Async
    public CompletableFuture<Void> changeOutbound(List<RetrievalMxEntity> mxs, String outBoundCode, Long userId) {
        OutboundEntity outbound = this.outboundService.lambdaQuery().eq(OutboundEntity::getCode, outBoundCode).last(CheckToolUtil.LIMIT).one();
        if (ObjUtil.isNotNull(outbound)) {
            Long id = outbound.getId();
            Map<Long, BigDecimal> map = mxs.stream().collect(Collectors.groupingBy(
                    RetrievalMxEntity::getGoodsId, Collectors.reducing(BigDecimal.ZERO, RetrievalMxEntity::getQty, BigDecimal::add)));
            List<OutboundMxEntity> list = this.outboundMxService.lambdaQuery().eq(OutboundMxEntity::getOutboundId, id).list();

            int status = 2;
            List<CountVo> mxVos = new ArrayList<>(map.size());
            List<CountVo> vos = this.retrievalMxService.getByObCode(outBoundCode);
            for (OutboundMxEntity mx : list) {
                Long goodsId = mx.getGoodsId();
                BigDecimal decimal = map.get(goodsId);
                if (decimal != null) {
                    CountVo countVo = vos.stream().filter(item -> item.getAId().equals(goodsId)).findFirst().orElse(null);
                    if (ObjUtil.isNotNull(countVo)) {
                        decimal = decimal.add(countVo.getQty());
                    }

                    mx.setActQty(decimal);
                    mxVos.add(new CountVo(id, goodsId, null, decimal));
                }

                BigDecimal actQty = mx.getActQty();
                if (ObjUtil.isNull(actQty) || mx.getQty().compareTo(mx.getActQty()) > 0) {
                    status = 4;
                }
            }

            // 质量出库创建保留品数据
            if (status == 2 && outbound.getType() == 21) {
                LocalDateTime now = LocalDateTime.now();
                String format = DateUtil.format(now, DatePattern.PURE_DATE_PATTERN);
                Long count = this.storageReservaService.lambdaQuery().between(StorageReserva::getCreateTime, LocalDateTimeUtil.beginOfDay(now),
                        LocalDateTimeUtil.endOfDay(now)).in(StorageReserva::getReturnType, 1, 2).count();

                List<StorageReserva> reservas = new ArrayList<>(mxs.size());
                StorageReserva reserva;
                for (OutboundMxEntity mx : list) {
                    reserva = new StorageReserva();
                    reserva.setId(IdUtil.getSnowflakeNextId());
                    reserva.setCode("ZL" + format + (101 + count++ * 3));
                    reserva.setPid(outbound.getId());
                    reserva.setReceiptCode(outbound.getCode());
                    reserva.setDetectionWay(1);
                    reserva.setGoodsId(mx.getGoodsId());
                    reserva.setType(mx.getType());
                    reserva.setUnqualifiedQty(mx.getActQty());
                    reserva.setDisposal(2);
                    reserva.setSource(2);
                    reserva.setTester(userId);
                    reserva.setCreateTime(now);
                    reservas.add(reserva);
                }

                this.storageReservaService.saveReservaBatch(reservas);
            }

            this.outboundService.lambdaUpdate().set(OutboundEntity::getStatus, status).eq(OutboundEntity::getId, id).update();
            this.outboundMxService.modifyBatch(mxVos);
            this.purchaseReturnsService.lambdaUpdate().set(PurchaseReturns::getStatus, 1).eq(PurchaseReturns::getId, outbound.getPid()).update();
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 变更出库明细表数据
     */
    @Async
    public CompletableFuture<Void> changeRetrievalMx(BigDecimal count, WorkOrderMxProcess process) {
        Long mxId = process.getMxId();
        Long stationId = process.getStationId();
        // 获取工单对应物料及其基础使用量
        List<Dict> list = this.workOrderMaterialService.getBaseMaterial(mxId, process.getProcessId(), count);
        if (CollUtil.isEmpty(list) || list.get(0) == null) {
            return CompletableFuture.completedFuture(null);
        }

        List<Long> materialIds = list.stream().map(item -> item.getLong("material_id")).collect(Collectors.toList());

        // 获取该种物料上料记录
        List<WorkOrderFeedEntity> feeds = this.workOrderFeedService.getByMaterials(mxId, stationId, materialIds);

        List<RetrievalMxEntity> entities = new ArrayList<>();
        // 计算使用量
        for (Dict dict : list) {
            Long materialId = dict.getLong("material_id");
            // 本次工序生产用到该物料的总量
            BigDecimal qty = dict.getBigDecimal("qty");

            List<WorkOrderFeedEntity> collect = feeds.stream()
                    .filter(item -> item.getMaterialId().equals(materialId))
                    .sorted(Comparator.comparing(WorkOrderFeedEntity::getCreateTime))
                    .collect(Collectors.toList());

            for (WorkOrderFeedEntity feed : collect) {
                // 扫码量 - 之前的使用量 - 本工序总量
                BigDecimal result = feed.getQty().subtract(feed.getUsedQty());
                BigDecimal subtract = result.subtract(qty);

                RetrievalMxEntity entity = new RetrievalMxEntity();
                entity.setQrCode(feed.getQrCode());
                if (subtract.compareTo(BigDecimal.valueOf(0)) >= 0) {
                    entity.setUsedQty(qty);
                    entities.add(entity);
                    break;
                } else {
                    entity.setUsedQty(result);
                    entities.add(entity);
                    qty = subtract.abs();
                }
            }
        }

        this.retrievalMxService.updateBatchByQrCode(entities);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 变更入库记录
     */
    @Async
    public CompletableFuture<Void> updateStorage(List<StorageEntity> storages, LocalDateTime now, Long userId) {
        this.storageService.modifyBatch(storages, now, userId);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 新增入库记录
     */
    @Async
    public CompletableFuture<Void> saveStorage(List<StorageEntity> storages, List<MoveVo> moveVos, LocalDateTime now, Long userId) {
        List<StorageEntity> entities = new ArrayList<>(storages.size());
        for (StorageEntity storage : storages) {
            MoveVo moveVo = moveVos.stream()
                    .filter(item -> item.getId().equals(storage.getId()))
                    .findFirst()
                    .orElse(null);

            if (moveVo != null) {
                storage.setResidueQty(moveVo.getRQty());
                entities.add(this.setValue(moveVo, storage, now, userId));
            }
        }

        this.storageService.insertBatch(entities);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 批量减库存量
     */
    @Async
    public CompletableFuture<Void> subStock(List<StorageEntity> storages, List<MoveVo> moveVos, LocalDateTime now, Long userId) {
        List<StockEntity> subStocks = new ArrayList<>(storages.size());
        StockEntity subStock;
        for (StorageEntity storage : storages) {
            MoveVo moveVo = moveVos.stream()
                    .filter(item -> item.getId().equals(storage.getId()))
                    .findFirst()
                    .orElse(null);

            if (moveVo != null) {
                subStock = new StockEntity();
                subStock.setId(storage.getStockId());
                subStock.setQty(moveVo.getRQty());
                subStocks.add(subStock);
            }
        }

        this.stockService.subModifyBatch(subStocks, now, userId);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 批量加库存量
     */
    @Async
    public CompletableFuture<Void> addStock(List<MoveVo> moveVos, LocalDateTime now, Long userId) {
        List<StockEntity> addStocks = new ArrayList<>(moveVos.size());
        StockEntity addStock;
        for (MoveVo moveVo : moveVos) {
            addStock = new StockEntity();
            addStock.setId(moveVo.getRStockId());
            addStock.setQty(moveVo.getRQty());
            addStocks.add(addStock);
        }

        this.stockService.addModifyBatch(addStocks, now, userId);
        return CompletableFuture.completedFuture(null);
    }

    private StorageEntity setValue(MoveVo moveVo, StorageEntity storage, LocalDateTime now, Long userId) {
        StorageEntity entity = new StorageEntity();
        entity.setId(IdUtil.getSnowflake().nextId());
        entity.setBatchNo(moveVo.getRBatchNo());
        entity.setSiCode(storage.getSiCode());
        entity.setGoodsId(storage.getGoodsId());
        entity.setType(storage.getType());
        entity.setQty(moveVo.getRQty());
        entity.setResidueQty(moveVo.getRQty());
        entity.setStatus(storage.getStatus());
        entity.setLifeType(storage.getLifeType());
        entity.setLifeInfo(storage.getLifeInfo());
        entity.setStockId(moveVo.getRStockId());
        entity.setRemarks(moveVo.getRRemarks());
        entity.setSource(3);
        entity.setCreateTime(storage.getCreateTime());
        entity.setCreateUser(userId);
        entity.setUpdateTime(now);
        entity.setUpdateUser(userId);
        return entity;
    }

}
