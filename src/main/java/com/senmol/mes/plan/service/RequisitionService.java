package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.RequisitionEntity;
import com.senmol.mes.plan.page.RequisitionPage;

import java.util.List;

/**
 * 请购单(Requisition)表服务接口
 *
 * @author makejava
 * @since 2023-03-13 15:57:22
 */
public interface RequisitionService extends IService<RequisitionEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param requisition 查询实体
     * @param keyword     关键字
     * @return 所有数据
     */
    SaResult selectAll(RequisitionPage page, RequisitionEntity requisition, String keyword);

    /**
     * 获取未绑定的请购单
     *
     * @return 列表
     */
    List<RequisitionEntity> getUnbound();

    /**
     * 新增数据
     *
     * @param requisition 实体对象
     * @return 新增结果
     */
    SaResult saveRequisition(RequisitionEntity requisition);

    /**
     * 批量新增
     *
     * @param requisitions 实体对象列表
     * @return 新增结果
     */
    SaResult saveRequisitions(List<RequisitionEntity> requisitions);

    /**
     * 修改数据
     *
     * @param requisition 实体对象
     * @return 修改结果
     */
    SaResult updateRequisition(RequisitionEntity requisition);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult removeRequisition(Long id);

}

