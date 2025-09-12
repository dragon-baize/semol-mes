package com.senmol.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.system.entity.RoleMenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色-菜单(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-03 09:06:17
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenuEntity> {

    /**
     * 批量保存
     *
     * @param entities 角色-菜单列表
     */
    void insertBatch(@Param("entities") List<RoleMenuEntity> entities);

}

