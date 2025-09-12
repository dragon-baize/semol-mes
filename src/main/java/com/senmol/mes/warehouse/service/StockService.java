package com.senmol.mes.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.vo.StockInfo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库位管理(Stock)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 17:08:53
 */
public interface StockService extends IService<StockEntity> {

    /**
     * 主键查询数据
     *
     * @param id   主键
     * @param type 物品类型
     * @return 单条数据
     */
    StockInfo selectOne(Long id, Integer type);

    /**
     * 批量根据ID修改减库存量
     *
     * @param stocks 实体列表
     */
    void subModifyBatch(List<StockEntity> stocks, LocalDateTime updateTime, Long updateUser);

    /**
     * 批量根据ID修改加库存量
     *
     * @param stocks 实体列表
     */
    void addModifyBatch(List<StockEntity> stocks, LocalDateTime updateTime, Long updateUser);

}

