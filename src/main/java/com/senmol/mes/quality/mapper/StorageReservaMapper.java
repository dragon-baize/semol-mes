package com.senmol.mes.quality.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.vo.StorageReservaVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 保留品记录(QualityStorageReserva)表数据库访问层
 *
 * @author makejava
 * @since 2023-12-20 09:32:46
 */
public interface StorageReservaMapper extends BaseMapper<StorageReserva> {

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param storageReserva 查询实体
     * @return 所有数据
     */
    List<StorageReservaVo> selectAll(Page<StorageReservaVo> page, @Param("sr") StorageReserva storageReserva);

    /**
     * 批量保存
     *
     * @param entities 实体对象列表
     */
    void saveReservaBatch(@Param("entities") List<StorageReserva> entities);

    /**
     * 当天已生成的code数量
     *
     * @param date   日期
     * @param source 数据来源 0-检测 1-新增 2-退货 3-过期
     * @return 数量
     */
    int getTodayCount(@Param("date") String date, @Param("source") Integer source);

}

