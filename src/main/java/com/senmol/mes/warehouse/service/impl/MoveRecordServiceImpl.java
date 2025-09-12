package com.senmol.mes.warehouse.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.warehouse.entity.MoveRecord;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.mapper.MoveRecordMapper;
import com.senmol.mes.warehouse.service.MoveRecordService;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.utils.WarAsyncUtil;
import com.senmol.mes.warehouse.vo.MoveVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    private WarAsyncUtil warAsyncUtil;

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

            CompletableFuture<Void> updateStorage = this.warAsyncUtil.updateStorage(storages, now, userId).exceptionally(e -> {
                e.printStackTrace();
                throw new BusinessException("库存剩余量变更失败");
            });

            CompletableFuture<Void> saveStorage = this.warAsyncUtil.saveStorage(storages, moveVos, now, userId).exceptionally(e -> {
                e.printStackTrace();
                throw new BusinessException("入库记录保存失败");
            });
            CompletableFuture<Void> subStock = this.warAsyncUtil.subStock(storages, moveVos, now, userId).exceptionally(e -> {
                e.printStackTrace();
                throw new BusinessException("库存量扣减失败");
            });

            CompletableFuture<Void> addStock = this.warAsyncUtil.addStock(moveVos, now, userId).exceptionally(e -> {
                e.printStackTrace();
                throw new BusinessException("库存量新增失败");
            });

            CompletableFuture.allOf(updateStorage, saveStorage, subStock, addStock).join();
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
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

