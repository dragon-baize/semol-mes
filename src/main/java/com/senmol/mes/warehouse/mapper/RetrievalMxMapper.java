package com.senmol.mes.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.vo.OutboundMxVo;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.vo.RetrievalMxVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库明细(RetrievalMx)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-24 16:54:04
 */
public interface RetrievalMxMapper extends BaseMapper<RetrievalMxEntity> {

    /**
     * 出库ID查出库明细
     *
     * @param retrievalId 出库ID
     * @param type        出库类型
     * @param sign        标识
     * @return 出库明细
     */
    List<RetrievalMxVo> getByRetrievalId(@Param("retrievalId") Long retrievalId,
                                         @Param("type") Integer type,
                                         @Param("sign") Integer sign);

    /**
     * 出库单查出库记录
     *
     * @param code 出库单编号
     * @return 出库记录列表
     */
    List<OutboundMxVo> getByOutBoundCode(@Param("code") String code);

    /**
     * 出库单查出库数量
     *
     * @param code 出库单编号
     * @return 出库数据
     */
    List<CountVo> getByObCode(@Param("code") String code);

    /**
     * 工单已领取数据量
     *
     * @param code 工单编号
     * @return 领取记录
     */
    List<CountVo> getByGdCode(@Param("code") String code);

    /**
     * 批量新增
     *
     * @param entities 实体列表
     */
    void insertBatch(@Param("entities") List<RetrievalMxEntity> entities);

    /**
     * 批量更新
     *
     * @param entities 更新数据
     */
    void updateBatchByQrCode(@Param("entities") List<RetrievalMxEntity> entities);

}

