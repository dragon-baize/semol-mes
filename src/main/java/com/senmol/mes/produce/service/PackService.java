package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.PackEntity;

/**
 * 包装管理(Pack)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface PackService extends IService<PackEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param pack 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<PackEntity> page, PackEntity pack);


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    PackEntity selectOne(Long id);

    /**
     * 新增数据
     *
     * @param pack 实体对象
     * @return 新增结果
     */
    SaResult insertPack(PackEntity pack);

    /**
     * 修改数据
     *
     * @param pack 实体对象
     * @return 修改结果
     */
    SaResult updatePack(PackEntity pack);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deletePack(Long id);
}

