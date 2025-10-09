package com.senmol.mes.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.common.utils.OutboundMaterial;
import com.senmol.mes.workorder.entity.WorkOrderMxMaterialEntity;
import com.senmol.mes.workorder.vo.OrderInfo;
import com.senmol.mes.workorder.vo.OrderMaterial;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 工单明细物料(WorkOrderMxMaterial)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-21 10:54:03
 */
public interface WorkOrderMxMaterialMapper extends BaseMapper<WorkOrderMxMaterialEntity> {

    /**
     * 工单明细查物料
     *
     * @param mxCode 工单号
     * @return 工单物料列表
     */
    List<OutboundMaterial> getMaterials(@Param("mxCode") String mxCode);

    /**
     * 查询工单未报工的物料
     *
     * @param mxId 工单ID
     * @return 未报工的物料ID列表
     */
    List<Long> getValidMaterial(@Param("mxId") Long mxId);

    /**
     * 工单明细ID查领取的物料
     *
     * @param mxId        工单明细ID
     * @param materialIds 物料ID列表
     * @return 物料列表
     */
    List<OrderMaterial> selectByMxId(@Param("mxId") Long mxId, @Param("materialIds") List<Long> materialIds);

    /**
     * 查询mrp对应工单
     *
     * @return 工单编号
     */
    List<CountVo> preInventory(@Param("materialId") Long materialId);

    /**
     * 查询物料占用库存
     *
     * @param ids 物料ID列表
     * @return 占用库存
     */
    List<CountVo> getMaterial(@Param("ids") Set<Long> ids);

    /**
     * 反向追溯
     *
     * @param batchNo 入库批次号
     * @return 结果
     */
    List<OrderInfo> retrospect(@Param("batchNo") String batchNo);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM work_order_mx WHERE is_free = 1 AND DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

