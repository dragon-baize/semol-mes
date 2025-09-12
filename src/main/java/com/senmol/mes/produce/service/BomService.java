package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.produce.entity.BomEntity;

import java.util.List;

/**
 * 物料清单(Bom)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:08
 */
public interface BomService extends IService<BomEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param bom  查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<BomEntity> page, BomEntity bom);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    SaResult selectOne(Long id);

    /**
     * 查询物料对应产品
     *
     * @param materialId 物料ID
     * @return 产品
     */
    List<CountVo> getProductId(Long materialId);

    /**
     * 新增数据
     *
     * @param bom 实体对象
     * @return 新增结果
     */
    SaResult insertBom(BomEntity bom);

    /**
     * 修改数据
     *
     * @param bom 实体对象
     * @return 修改结果
     */
    SaResult updateBom(BomEntity bom);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteBom(Long id);

}

