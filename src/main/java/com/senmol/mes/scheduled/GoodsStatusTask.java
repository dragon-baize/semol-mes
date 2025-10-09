package com.senmol.mes.scheduled;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.entity.OutboundMxEntity;
import com.senmol.mes.plan.service.OutboundMxService;
import com.senmol.mes.plan.service.OutboundService;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.service.StorageReservaService;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.service.UserService;
import com.senmol.mes.warehouse.entity.RetrievalEntity;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.warehouse.service.RetrievalService;
import com.senmol.mes.warehouse.service.StockService;
import com.senmol.mes.warehouse.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 定时修改原料过期状态
 *
 * @author Administrator
 */
@Configuration
@Slf4j
public class GoodsStatusTask {

    @Resource
    private StorageService storageService;
    @Resource
    private StorageReservaService storageReservaService;
    @Resource
    private UserService userService;
    @Resource
    private OutboundService outboundService;
    @Resource
    private OutboundMxService outboundMxService;
    @Resource
    private RetrievalService retrievalService;
    @Resource
    private RetrievalMxService retrievalMxService;
    @Resource
    private StockService stockService;

    @Value("${storage.advent.duration:30}")
    private Integer adventDuration;

    /**
     * 每天执行一次修改原料过期状态
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Scheduled(cron = "${storage.changeStatus.cron}")
    public void changeStatus() {
        LocalDateTime now = LocalDateTime.now();
        log.info(now + "：执行修改物品过期状态");

        // 查询自动维护、未过期、剩余量大于0的入库数据
        List<StorageEntity> storages = this.storageService.lambdaQuery().eq(StorageEntity::getLifeType, 1)
                .isNotNull(StorageEntity::getLifeInfo).gt(StorageEntity::getStatus, 0).gt(StorageEntity::getResidueQty, 0).list();
        if (CollUtil.isEmpty(storages)) {
            log.info(LocalDateTime.now() + "：执行完成修改物品过期状态");
            return;
        }

        this.deal(storages, now);
        if (CollUtil.isEmpty(storages)) {
            log.info(LocalDateTime.now() + "：执行完成修改物品过期状态");
            return;
        }

        long userId = this.userService.lambdaQuery().eq(UserEntity::getUsername, "admin").one().getId();
        this.storageService.updateStatusByIds(storages, now, userId);

        String date = LocalDate.now().toString();
        String format = date.replace("-", "");
        OutboundEntity outbound = this.createOutBound(storages, format, now, date, userId);

        AtomicInteger count = new AtomicInteger(0);
        int total = this.retrievalService.getTodayCount(date);
        try {
            this.createRetrieval(new ArrayList<>(storages), outbound, 0, count, total, userId, now, format);
        } catch (Exception e) {
            throw new BusinessException("成品出库数据生成失败");
        }

        try {
            this.createRetrieval(new ArrayList<>(storages), outbound, 1, count, total, userId, now, format);
        } catch (Exception e) {
            throw new BusinessException("半成品出库数据生成失败");
        }

        try {
            this.createRetrieval(new ArrayList<>(storages), outbound, 2, count, total, userId, now, format);
        } catch (Exception e) {
            throw new BusinessException("原料出库数据生成失败");
        }

        try {
            this.createRetrieval(new ArrayList<>(storages), outbound, 3, count, total, userId, now, format);
        } catch (Exception e) {
            throw new BusinessException("非原料出库数据生成失败");
        }

        try {
            this.createReserva(new ArrayList<>(storages), outbound, userId, format, now, date);
        } catch (Exception e) {
            throw new BusinessException("保留品记录生成失败");
        }

        log.info(LocalDateTime.now() + "：执行完成修改物品过期状态");
    }

    private void deal(List<StorageEntity> storages, LocalDateTime now) {
        ListIterator<StorageEntity> iterator = storages.listIterator();
        while (iterator.hasNext()) {
            StorageEntity storage = iterator.next();
            // 创建日期
            LocalDateTime createTime = LocalDateTimeUtil.beginOfDay(storage.getCreateTime());
            LocalDateTime offset = LocalDateTimeUtil.offset(createTime, storage.getLifeInfo(), ChronoUnit.DAYS);
            // 设置将过期：默认30天
            long between = LocalDateTimeUtil.between(now, offset, ChronoUnit.DAYS);
            if (Math.abs(between) <= adventDuration && storage.getStatus() != 2) {
                storage.setStatus(2);
            } else if (offset.isBefore(now)) {
                // 寿命截止日期小于等于当前日期为过期
                storage.setStatus(0);
                storage.setQty(storage.getResidueQty());
                storage.setResidueQty(BigDecimal.ZERO);
            } else {
                iterator.remove();
            }
        }
    }

    /**
     * 创建出库单
     */
    private OutboundEntity createOutBound(List<StorageEntity> storages, String format, LocalDateTime now, String date,
                                          Long userId) {
        storages.removeIf(item -> item.getStatus() != 0);
        if (CollUtil.isEmpty(storages)) {
            return null;
        }

        int count = this.outboundService.getTodayCount(date);
        OutboundEntity outbound = new OutboundEntity();
        long id = IdUtil.getSnowflakeNextId();
        String code = "CKD" + format + (101 + count * 3);
        outbound.setId(id);
        outbound.setCode(code);
        outbound.setType(22);
        outbound.setStatus(2);
        outbound.setCreateUser(userId);
        outbound.setCreateTime(now);
        outbound.setUpdateTime(now);
        outbound.setUpdateUser(userId);
        boolean b = this.outboundService.saveOutbound(outbound);
        if (b) {
            List<OutboundMxEntity> list = new ArrayList<>(storages.size());
            OutboundMxEntity outboundMx;
            for (StorageEntity storage : storages) {
                outboundMx = new OutboundMxEntity();
                outboundMx.setOutboundId(id);
                outboundMx.setStorageId(storage.getId());
                outboundMx.setGoodsId(storage.getGoodsId());
                outboundMx.setType(storage.getType());
                outboundMx.setQty(storage.getQty());
                outboundMx.setActQty(storage.getQty());

                list.add(outboundMx);
            }

            this.outboundMxService.saveBatch(list);
        }

        return outbound;
    }

    /**
     * 创建出库数据
     */
    private void createRetrieval(List<StorageEntity> storages, OutboundEntity outbound, Integer type,
                                 AtomicInteger count,
                                 int total, Long userId, LocalDateTime now, String format) {
        storages.removeIf(item -> item.getStatus() != 0 || !item.getType().equals(type));
        if (CollUtil.isEmpty(storages)) {
            return;
        }

        int increment = count.getAndIncrement();
        RetrievalEntity retrieval = new RetrievalEntity();
        long id = IdUtil.getSnowflakeNextId();
        retrieval.setId(id);
        retrieval.setBatchNo("CK" + format + (101 + (total + increment) * 3));
        retrieval.setPickOrder(outbound.getCode());
        retrieval.setPicker(userId);
        retrieval.setType(type);
        retrieval.setCreateUser(userId);
        retrieval.setCreateTime(now);
        retrieval.setUpdateUser(userId);
        retrieval.setUpdateTime(now);
        boolean b = this.retrievalService.saveRetrieval(retrieval);
        if (b) {
            String qrCode = DateUtil.format(now, DatePattern.PURE_DATETIME_MS_PATTERN);
            int size = storages.size();
            List<RetrievalMxEntity> list = new ArrayList<>(size);
            RetrievalMxEntity retrievalMx;
            Map<Long, BigDecimal> map = new HashMap<>((int) (size / 0.75 + 1));
            for (int i = 0, j = storages.size(); i < j; i++) {
                StorageEntity storage = storages.get(i);
                Long stockId = storage.getStockId();
                BigDecimal qty = storage.getQty();

                retrievalMx = new RetrievalMxEntity();
                retrievalMx.setRetrievalId(id);
                retrievalMx.setStorageId(storage.getId());
                retrievalMx.setGoodsId(storage.getGoodsId());
                retrievalMx.setQrCode(qrCode + (1001 + (i + increment) * 3));
                retrievalMx.setType(type);
                retrievalMx.setQty(qty);
                retrievalMx.setRequireQty(qty);
                retrievalMx.setResidueQty(qty);
                retrievalMx.setStockId(stockId);
                retrievalMx.setCreateUser(userId);
                retrievalMx.setCreateTime(now);
                retrievalMx.setUpdateUser(userId);
                retrievalMx.setUpdateTime(now);

                map.merge(stockId, qty, BigDecimal::add);
                list.add(retrievalMx);
            }

            this.retrievalMxService.insertBatch(list);

            List<StockEntity> collect = map.entrySet().stream().map(item -> {
                StockEntity stock = new StockEntity();
                stock.setId(item.getKey());
                stock.setQty(item.getValue());
                return stock;
            }).collect(Collectors.toList());
            this.stockService.subModifyBatch(collect, now, userId);
        }
    }

    /**
     * 创建保留品数据
     */
    private void createReserva(List<StorageEntity> storages, OutboundEntity outbound, Long userId, String format,
                               LocalDateTime now, String date) {
        storages.removeIf(item -> item.getStatus() != 0);
        if (CollUtil.isEmpty(storages)) {
            return;
        }

        // 记录过期数据
        List<StorageReserva> reservas = new ArrayList<>(storages.size());
        StorageReserva reserva;
        int count = this.storageReservaService.getTodayCount(date, 3);
        Long outboundId = outbound.getId();
        String code = outbound.getCode();
        for (StorageEntity storage : storages) {
            reserva = new StorageReserva();
            reserva.setId(IdUtil.getSnowflakeNextId());
            reserva.setCode("GQ" + format + (101 + count++ * 3));
            reserva.setPid(outboundId);
            reserva.setReceiptCode(code);
            reserva.setBatchNo(storage.getBatchNo());
            reserva.setDetectionWay(1);
            reserva.setGoodsId(storage.getGoodsId());
            reserva.setType(storage.getType());
            reserva.setUnqualifiedQty(storage.getQty());
            reserva.setDisposal(2);
            reserva.setSource(3);
            reserva.setTester(userId);
            reserva.setCreateTime(now);
            reserva.setCreateUser(userId);
            reserva.setUpdateTime(now);
            reserva.setUpdateUser(userId);
            reservas.add(reserva);
        }

        this.storageReservaService.saveReservaBatch(reservas);
    }

}
