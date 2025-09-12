package com.senmol.mes.warehouse.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.mapper.StockMapper;
import com.senmol.mes.warehouse.service.StockService;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.vo.StockInfo;
import com.senmol.mes.warehouse.vo.StorageVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 库位管理(Stock)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 17:08:53
 */
@Service("stockService")
public class StockServiceImpl extends ServiceImpl<StockMapper, StockEntity> implements StockService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private StorageService storageService;

    @Override
    public StockInfo selectOne(Long id, Integer type) {
        StockEntity stock = this.getById(id);
        StockInfo stockInfo = Convert.convert(StockInfo.class, stock);

        List<StorageVo> storageVos = this.storageService.getByStockId(id, type);
        storageVos.forEach(item -> item.setUnitTitle(this.sysFromRedis.getDictMx(item.getUnitId())));
        stockInfo.setStorageVos(storageVos);
        return stockInfo;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void subModifyBatch(List<StockEntity> stocks, LocalDateTime updateTime, Long updateUser) {
        this.baseMapper.subModifyBatch(stocks, updateTime, updateUser);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addModifyBatch(List<StockEntity> stocks, LocalDateTime updateTime, Long updateUser) {
        this.baseMapper.addModifyBatch(stocks, updateTime, updateUser);
    }
}

