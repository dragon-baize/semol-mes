package com.senmol.mes.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.vo.OutboundMxVo;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.vo.RetrievalMxVo;

import java.util.List;

/**
 * 出库明细(RetrievalMx)表服务接口
 *
 * @author makejava
 * @since 2023-07-24 16:54:04
 */
public interface RetrievalMxService extends IService<RetrievalMxEntity> {

    /**
     * 出库ID查出库明细
     *
     * @param pickOrder   出库单编号
     * @param retrievalId 出库ID
     * @param type        出库类型
     * @return 出库明细
     */
    List<RetrievalMxVo> getByRetrievalId(String pickOrder, Long retrievalId, Integer type);

    /**
     * 出库单查出库记录
     *
     * @param code 出库单编号
     * @return 出库记录列表
     */
    List<OutboundMxVo> getByOutBoundCode(String code);

    /**
     * 出库单查出库数量
     *
     * @param code 出库单编号
     * @return 出库数据
     */
    List<CountVo> getByObCode(String code);

    /**
     * 工单已领取数据量
     *
     * @param code 工单编号
     * @return 领取记录
     */
    List<CountVo> getByGdCode(String code);

    /**
     * 批量新增
     *
     * @param list 实体列表
     */
    void insertBatch(List<RetrievalMxEntity> list);

    /**
     * 批量更新
     *
     * @param entities 更新数据
     */
    void updateBatchByQrCode(List<RetrievalMxEntity> entities);

}
