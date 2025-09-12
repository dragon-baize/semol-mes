package com.senmol.mes.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.workorder.entity.WorkOrderFeedEntity;
import com.senmol.mes.workorder.page.Retrospect;
import com.senmol.mes.workorder.vo.FeedInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 上料记录(WorkOrderFeed)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-23 10:44:08
 */
public interface WorkOrderFeedMapper extends BaseMapper<WorkOrderFeedEntity> {

    /**
     * 上料记录
     *
     * @param mxId       工单ID
     * @param stationId  工位ID
     * @param materialId 物料ID
     * @return 结果
     */
    List<FeedInfo> getByMxId(@Param("mxId") Long mxId,
                             @Param("stationId") Long stationId,
                             @Param("materialId") Long materialId);

    /**
     * 上料记录
     *
     * @param mxId        工单ID
     * @param stationId   工位ID
     * @param materialIds 物料ID
     * @return 结果
     */
    List<WorkOrderFeedEntity> getByMaterials(@Param("mxId") Long mxId,
                                             @Param("stationId") Long stationId,
                                             @Param("materialIds") List<Long> materialIds);

    /**
     * 分页查询
     *
     * @param retrospect 查询对象
     * @return 结果
     */
    List<Retrospect> retrospect(@Param("arg") Retrospect retrospect);

    /**
     * 查询物料使用量
     *
     * @param qrCodes 出库物料唯一码
     * @return 结果
     */
    List<Retrospect> getUseQty(@Param("qrCodes") List<String> qrCodes);

}

