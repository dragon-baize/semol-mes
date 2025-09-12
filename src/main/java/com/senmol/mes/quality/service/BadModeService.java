package com.senmol.mes.quality.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.quality.entity.BadModeEntity;

/**
 * 不良模式(BadMode)表服务接口
 *
 * @author makejava
 * @since 2023-01-31 09:15:54
 */
public interface BadModeService extends IService<BadModeEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param badMode 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<BadModeEntity> page, BadModeEntity badMode);

    /**
     * 新增数据
     *
     * @param badMode 实体对象
     * @return 新增结果
     */
    SaResult insertBadMode(BadModeEntity badMode);

    /**
     * 修改数据
     *
     * @param badMode 实体对象
     * @return 修改结果
     */
    SaResult updateBadMode(BadModeEntity badMode);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult delBadMode(Long id);
}

