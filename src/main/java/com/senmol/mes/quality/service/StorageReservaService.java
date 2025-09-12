package com.senmol.mes.quality.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.vo.StorageReservaVo;

import java.util.List;

/**
 * 保留品记录(QualityStorageReserva)表服务接口
 *
 * @author makejava
 * @since 2023-12-20 09:32:46
 */
public interface StorageReservaService extends IService<StorageReserva> {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return 所有数据
     */
    SaResult selectOne(Long id);

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param storageReserva 查询实体
     * @return 所有数据
     */
    Page<StorageReservaVo> selectAll(Page<StorageReservaVo> page, StorageReserva storageReserva);

    /**
     * 新增数据
     *
     * @param reservas 实体对象列表
     * @return 新增结果
     */
    SaResult insertBatch(List<StorageReserva> reservas);

    /**
     * 批量保存
     *
     * @param reservas 实体对象列表
     */
    void saveReservaBatch(List<StorageReserva> reservas);

    /**
     * 修改数据
     *
     * @param storageReserva 实体对象
     * @return 修改结果
     */
    SaResult updateReserva(StorageReserva storageReserva);

    /**
     * 批量修改
     *
     * @param storageReservas 对象列表
     * @return 修改结果
     */
    SaResult modifyBatch(List<StorageReserva> storageReservas);


}

