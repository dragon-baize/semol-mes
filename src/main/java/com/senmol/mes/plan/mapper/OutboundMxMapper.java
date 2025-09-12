package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.OutboundMxEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 出库单明细(OutboundMx)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-28 18:39:11
 */
public interface OutboundMxMapper extends BaseMapper<OutboundMxEntity> {

    /**
     * 查询工单出库数量
     *
     * @param codes 工单编号
     * @param ids   物料ID
     * @param sign  出库类型 0-生产 1-委外
     * @param type  物品类型 0-成品 1-半成品 2-原料 3-非原料
     * @return 出库数量
     */
    List<CountVo> getCkQty(@Param("codes") Set<String> codes,
                           @Param("ids") Set<Long> ids,
                           @Param("sign") Integer sign,
                           @Param("type") Integer type);

    /**
     * 查询工单、委外出库数量
     *
     * @param codes      工单、委外编号
     * @param materialId 物料ID
     * @param type       出库类型 0-生产 1-委外
     * @return 出库数量
     */
    List<CountVo> getSckQty(@Param("codes") Set<String> codes, @Param("materialId") Long materialId,
                            @Param("type") Integer type);

    /**
     * 出库单编号查询出库记录
     *
     * @param code 出库单编号
     * @return 出库记录
     */
    List<OutboundMxEntity> getByCode(@Param("code") String code);

    /**
     * 查询未完成出库的存货
     *
     * @param outboundId 出库单ID
     * @return 结果
     */
    List<Long> getNotShip(@Param("outboundId") Long outboundId);

    /**
     * 批量更新
     *
     * @param mxVos 出库单明细
     */
    void modifyBatch(@Param("mxVos") List<CountVo> mxVos);

}

