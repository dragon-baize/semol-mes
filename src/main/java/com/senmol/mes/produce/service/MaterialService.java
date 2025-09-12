package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.MaterialEntity;
import com.senmol.mes.produce.vo.MaterialVo;

import java.util.List;

/**
 * 物料管理(Material)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface MaterialService extends IService<MaterialEntity> {

    /**
     * 查询所有数据
     *
     * @param supplierId 供应商ID
     * @return 所有数据
     */
    List<MaterialVo> getList(Long supplierId, Integer type);

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param material 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<MaterialEntity> page, MaterialEntity material);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    SaResult selectOne(Long id);

    /**
     * 新增数据
     *
     * @param material 实体对象
     * @return 新增结果
     */
    SaResult insertMaterial(MaterialEntity material);

    /**
     * Excel导入
     *
     * @param cachedDataList 导入数据
     * @return 失败数据列表
     */
    List<Object> insertByExcel(List<Object> cachedDataList);

    /**
     * 修改数据
     *
     * @param material 实体对象
     * @return 修改结果
     */
    SaResult updateMaterial(MaterialEntity material);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult delMaterial(Long id);

}

