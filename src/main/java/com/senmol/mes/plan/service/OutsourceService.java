package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.OutboundMaterial;
import com.senmol.mes.plan.entity.OutsourceEntity;

import java.util.List;

/**
 * 委外计划(Outsource)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
public interface OutsourceService extends IService<OutsourceEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param outsource 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<OutsourceEntity> page, OutsourceEntity outsource);

    /**
     * 查询在制委外量
     */
    SaResult inOutsource(List<Long> productIds, Long saleOrderId);

    /**
     * 通过计划编号查询单条数据
     *
     * @param code 计划编号
     * @return 单条数据
     */
    List<OutboundMaterial> getByCode(String code);

    /**
     * 检测号查委外计划
     */
    OutsourceEntity getBySiCode(String siCode);

    /**
     * 查询委外计划信息
     *
     * @param productIds 产品ID
     * @return 委外计划信息
     */
    List<OutsourceEntity> getByCpId(List<Long> productIds);

    /**
     * 新增数据
     *
     * @param outsource 实体对象
     * @return 新增结果
     */
    SaResult insertOutsource(OutsourceEntity outsource);

    /**
     * 批量新增
     *
     * @param outsources 实体对象
     * @return 新增结果
     */
    SaResult insertOutsources(List<OutsourceEntity> outsources);

    /**
     * 修改数据
     *
     * @param outsource 实体对象
     * @return 修改结果
     */
    SaResult updateOutsource(OutsourceEntity outsource);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteOutsource(Long id);

}

