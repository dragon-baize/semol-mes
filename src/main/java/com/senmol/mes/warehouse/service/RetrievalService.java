package com.senmol.mes.warehouse.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.warehouse.entity.RetrievalEntity;
import com.senmol.mes.warehouse.vo.RetrievalInfo;

/**
 * 出库记录(Retrieval)表服务接口
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
public interface RetrievalService extends IService<RetrievalEntity> {

    /**
     * 主键查询单条数据
     *
     * @param id   主键
     * @param type 类型
     * @return 单条数据
     */
    RetrievalEntity selectOne(Long id, Integer type);

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param retrieval 查询实体
     * @return 所有数据
     */
    Page<RetrievalEntity> selectAll(Page<RetrievalEntity> page, RetrievalEntity retrieval);

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    SaResult insertRetrievalInfo(RetrievalInfo info);

    /**
     * 新增数据
     *
     * @param retrieval 实体对象
     * @return 新增结果
     */
    boolean saveRetrieval(RetrievalEntity retrieval);

}

