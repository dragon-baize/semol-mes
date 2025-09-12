package com.senmol.mes.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.workflow.entity.FlowRecord;
import com.senmol.mes.workflow.vo.FlowRecordInfo;
import com.senmol.mes.workflow.vo.FlowRecordVo;

/**
 * 审核记录(FlowRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
public interface FlowRecordService extends IService<FlowRecord> {
    /**
     * 查询流程详细
     *
     * @param id 主键
     * @return 流程详细
     */
    FlowRecordInfo selectOne(Long id);

    /**
     * 分页查询所有数据
     *
     * @param page       分页对象
     * @param flowRecord 查询实体
     * @return 所有数据
     */
    Page<FlowRecordVo> selectAll(Page<FlowRecordVo> page, FlowRecord flowRecord);

    /**
     * 开启流程
     *
     * @param flowRecord 实体对象
     */
    void insertFlowRecord(FlowRecord flowRecord);

    void updateStatus(Object table, Integer status, Object id);

    /**
     * 删除流程
     *
     * @param itemId 项目ID
     */
    void deleteFlowRecord(Long itemId);

}

