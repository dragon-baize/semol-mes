package com.senmol.mes.warehouse.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.warehouse.entity.MoveRecord;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.mapper.MoveRecordMapper;
import com.senmol.mes.warehouse.service.MoveRecordService;
import com.senmol.mes.warehouse.service.StockService;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.vo.MoveVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 迁库记录(MoveRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-12-21 11:24:51
 */
@Service("moveRecordService")
public class MoveRecordServiceImpl extends ServiceImpl<MoveRecordMapper, MoveRecord> implements MoveRecordService {

    @Resource
    private StorageService storageService;
    @Resource
    private StockService stockService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SaResult move(List<MoveVo> moveVos) {
        List<MoveVo> errorList = new ArrayList<>();

        // 校验批次号
        this.checkBatchNo(moveVos, errorList);

        if (CollUtil.isEmpty(moveVos)) {
            return SaResult.data(errorList);
        }

        List<Long> ids = moveVos.stream()
                .map(MoveVo::getId)
                .collect(Collectors.toList());

        List<StorageEntity> storages = this.storageService.lambdaQuery().in(StorageEntity::getId, ids).list();

        List<MoveRecord> records = new ArrayList<>(storages.size());
        MoveRecord record;
        for (StorageEntity storage : storages) {
            MoveVo moveVo = moveVos.stream()
                    .filter(item -> item.getId().equals(storage.getId()))
                    .findFirst()
                    .orElse(null);

            if (moveVo != null) {
                storage.setResidueQty(moveVo.getRQty());

                record = new MoveRecord();
                record.setOutBatchNo(storage.getBatchNo());
                record.setOutStockId(storage.getStockId());
                record.setInBatchNo(moveVo.getRBatchNo());
                record.setInStockId(moveVo.getRStockId());
                record.setQty(moveVo.getRQty());
                records.add(record);
            }
        }

        // 新增变更记录
        boolean b = this.saveBatch(records);
        if (b) {
            // 异步修改入库、库存记录
            LocalDateTime now = LocalDateTime.now();
            long userId = StpUtil.getLoginIdAsLong();

            // 变更入库记录
            this.storageService.modifyBatch(storages, now, userId);

            try {
                this.saveStorage(storages, moveVos, now, userId);
            } catch (Exception e) {
                log.error("入库记录保存失败", e);
                throw new BusinessException("入库记录保存失败");
            }

            try {
                this.subStock(storages, moveVos, now, userId);
            } catch (Exception e) {
                log.error("库存量扣减失败", e);
                throw new BusinessException("库存量扣减失败");
            }

            try {
                this.addStock(moveVos, now, userId);
            } catch (Exception e) {
                log.error("库存量新增失败", e);
                throw new BusinessException(e);
            }
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    /**
     * 新增入库记录
     */
    private void saveStorage(List<StorageEntity> storages, List<MoveVo> moveVos, LocalDateTime now, Long userId) {
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
    }

    /**
     * 批量减库存量
     */
    private void subStock(List<StorageEntity> storages, List<MoveVo> moveVos, LocalDateTime now, Long userId) {
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
    }

    /**
     * 批量加库存量
     */
    private void addStock(List<MoveVo> moveVos, LocalDateTime now, Long userId) {
        List<StockEntity> addStocks = new ArrayList<>(moveVos.size());
        StockEntity addStock;
        for (MoveVo moveVo : moveVos) {
            addStock = new StockEntity();
            addStock.setId(moveVo.getRStockId());
            addStock.setQty(moveVo.getRQty());
            addStocks.add(addStock);
        }

        this.stockService.addModifyBatch(addStocks, now, userId);
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

    private void checkBatchNo(List<MoveVo> moveVos, List<MoveVo> errorList) {
        List<String> batchNos = moveVos.stream()
                .map(MoveVo::getRBatchNo)
                .collect(Collectors.toList());

        List<StorageEntity> list = this.storageService.lambdaQuery().in(StorageEntity::getBatchNo, batchNos).list();
        if (CollUtil.isNotEmpty(list)) {
            for (StorageEntity entity : list) {
                moveVos.stream()
                        .filter(item -> item.getRBatchNo().equals(entity.getBatchNo()))
                        .findFirst()
                        .ifPresent(item -> {
                            item.setRRemarks("批次号重复");
                            errorList.add(item);
                        });
            }
        }

        moveVos.removeAll(errorList);
    }

}

