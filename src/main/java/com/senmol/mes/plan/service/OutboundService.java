package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.vo.OutboundInfo;
import com.senmol.mes.plan.vo.OutboundVo;
import com.senmol.mes.plan.vo.ProductQty;

import java.util.List;
import java.util.Set;

/**
 * 出库单(Outbound)表服务接口
 *
 * @author makejava
 * @since 2023-03-13 10:21:09
 */
public interface OutboundService extends IService<OutboundEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param outbound 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<OutboundVo> page, OutboundEntity outbound);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    SaResult selectOne(Long id);

    /**
     * 查询出库单明细
     *
     * @param id 出库单ID
     * @return 明细列表
     */
    SaResult mx(Long id);

    /**
     * 通过单号查询单条数据
     *
     * @param code      单号
     * @return 单条数据
     */
    SaResult getByCode(String code);

    /**
     * 销售未完成订单量
     *
     * @param productIds 产品ID列表
     * @return 产品-数量
     */
    List<ProductQty> getSumQty(List<Long> productIds);

    /**
     * 查询物料已出库数量
     *
     * @param codes 工单编号
     * @param ids   物料ID
     * @return 物料已出库数量
     */
    List<CountVo> getOutBoundQty(Set<String> codes, Set<Long> ids);

    /**
     * 查询物料已出库数量
     *
     * @param codes      工单编号
     * @param materialId 物料ID
     * @return 物料已出库数量
     */
    List<CountVo> getObQty(Set<String> codes, Long materialId);

    /**
     * 查询物料委外出库单未出库数量
     *
     * @param ids 物料ID
     * @return MRP占用库存
     */
    List<CountVo> getOutsourceQty(Set<Long> ids);

    /**
     * 查询物料委外出库单未出库数量
     *
     * @param materialId 物料ID
     * @return MRP占用库存
     */
    List<CountVo> getOsQty(Long materialId);

    /**
     * 新增数据
     *
     * @param outbound 实体对象
     * @return 新增结果
     */
    SaResult insertOutbound(OutboundVo outbound);

    /**
     * 新增数据
     *
     * @param outbound 实体对象
     */
    boolean saveOutbound(OutboundEntity outbound);

    /**
     * 新增发货单
     *
     * @param outbound 实体对象
     * @return 新增结果
     */
    SaResult insertInvoice(OutboundInfo outbound);

    /**
     * 修改数据
     *
     * @param outboundVo 实体对象
     * @return 修改结果
     */
    SaResult updateOutbound(OutboundVo outboundVo);

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    SaResult deleteOutbound(List<Long> idList);

}

