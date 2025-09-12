package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.CustomEntity;

import java.util.List;

/**
 * 客户管理(Custom)表服务接口
 *
 * @author makejava
 * @since 2023-07-13 16:18:45
 */
public interface CustomService extends IService<CustomEntity> {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return 客户信息
     */
    CustomEntity selectOne(Long id);

    /**
     * 分页查询所有数据
     *
     * @param page   分页对象
     * @param custom 查询实体
     * @return 所有数据
     */
    Page<CustomEntity> selectAll(Page<CustomEntity> page, CustomEntity custom);

    /**
     * 产品ID查客户
     *
     * @param productId 产品ID
     * @return 客户
     */
    List<CustomEntity> getByProductId(Long productId);

    /**
     * 新增数据
     *
     * @param custom 实体对象
     * @return 新增结果
     */
    SaResult insertCustom(CustomEntity custom);

    /**
     * 修改数据
     *
     * @param custom 实体对象
     * @return 修改结果
     */
    SaResult updateCustom(CustomEntity custom);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteCustom(Long id);

}

