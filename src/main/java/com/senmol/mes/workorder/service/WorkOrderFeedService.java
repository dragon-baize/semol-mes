package com.senmol.mes.workorder.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.workorder.entity.WorkOrderFeedEntity;
import com.senmol.mes.workorder.page.Retrospect;

import java.util.List;

/**
 * 上料记录(WorkOrderFeed)表服务接口
 *
 * @author makejava
 * @since 2023-10-23 10:44:08
 */
public interface WorkOrderFeedService extends IService<WorkOrderFeedEntity> {

    /**
     * 上料记录
     *
     * @param mxId       工单ID
     * @param stationId  工位ID
     * @param materialId 物料ID
     * @return 结果
     */
    SaResult getByMxId(Long mxId, Long stationId, Long materialId);

    /**
     * 上料记录
     *
     * @param mxId        工单ID
     * @param stationId   工位ID
     * @param materialIds 物料ID
     * @return 结果
     */
    List<WorkOrderFeedEntity> getByMaterials(Long mxId, Long stationId, List<Long> materialIds);

    /**
     * 原材料用量追溯
     *
     * @param retrospect 查询对象
     * @return 结果
     */
    SaResult retrospect(Retrospect retrospect);

    /**
     * 新增数据
     *
     * @param workOrderFeed 实体对象
     * @return 新增结果
     */
    SaResult insertFeed(WorkOrderFeedEntity workOrderFeed);

}

