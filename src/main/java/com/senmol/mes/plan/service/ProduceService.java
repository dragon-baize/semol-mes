package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.vo.ProduceVo;
import com.senmol.mes.plan.vo.ProductQty;
import com.senmol.mes.workorder.vo.MaterialPojo;
import com.senmol.mes.workorder.vo.ProductLineInfo;
import com.senmol.mes.workorder.vo.WorkOrderPojo;

import java.util.List;
import java.util.Set;

/**
 * 生产计划(Produce)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
public interface ProduceService extends IService<ProduceEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param produce 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<ProduceVo> page, ProduceEntity produce);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    ProduceVo selectOne(Long id);

    /**
     * 查询产线计划产品
     *
     * @param productLineId 产线ID
     * @return 结果
     */
    List<ProduceVo> byProductLineId(Long productLineId);

    /**
     * 产品计划生产总量
     *
     * @param productIds 产品ID列表
     * @return 总量
     */
    List<ProductQty> getSumQty(List<Long> productIds, Long saleOrderId);

    /**
     * 查询任务单对应计划信息
     *
     * @param ids 任务单ID
     * @return 计划信息
     */
    List<MaterialPojo> getPlanInfo(Set<Long> ids);

    /**
     * 工作台统计
     */
    SaResult workbench();

    /**
     * 产品在制生产量
     *
     * @param productIds 产品ID列表
     * @return 结果
     */
    SaResult inProcess(List<Long> productIds);

    /**
     * 工作台产线列表
     *
     * @param productLineId 产线ID
     * @return 产线列表
     */
    List<ProductLineInfo> statOrder(Long productLineId);

    /**
     * 工单编号查生产计划数据
     *
     * @param mxCode 工单编号
     * @return 生产计划数据
     */
    WorkOrderPojo getByMxCode(String mxCode);

    /**
     * 工单ID查生产计划数据
     *
     * @param mxId 工单ID
     * @return 生产计划数据
     */
    WorkOrderPojo getByMxId(Long mxId);

    /**
     * 新增数据
     *
     * @param produce 实体对象
     * @return 新增结果
     */
    SaResult insertProduce(ProduceEntity produce);

    /**
     * 批量新增
     *
     * @param produces 实体对象
     * @return 新增结果
     */
    SaResult insertProduces(List<ProduceEntity> produces);

    /**
     * 修改数据
     *
     * @param produce 实体对象
     * @return 修改结果
     */
    SaResult updateProduce(ProduceEntity produce);

    /**
     * 设置计划完成
     *
     * @param produce 实体对象
     * @return 处理结果
     */
    SaResult closeOrder(ProduceEntity produce);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteProduce(Long id);

}

