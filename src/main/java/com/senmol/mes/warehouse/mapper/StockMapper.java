package com.senmol.mes.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.warehouse.entity.StockEntity;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库位管理(Stock)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 17:08:53
 */
public interface StockMapper extends BaseMapper<StockEntity> {
    /**
     * 批量根据ID修改减库存量
     *
     * @param stocks 实体列表
     */
    void subModifyBatch(@Param("stocks") List<StockEntity> stocks,
                        @Param("updateTime") LocalDateTime updateTime,
                        @Param("updateUser") Long updateUser);

                        /**
     * 批量根据ID修改加库存量
     *
     * @param stocks 实体列表
     */
    void addModifyBatch(@Param("stocks") List<StockEntity> stocks,
                        @Param("updateTime") LocalDateTime updateTime,
                        @Param("updateUser") Long updateUser);

}

