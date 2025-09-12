package com.senmol.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.system.entity.RolePermissionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限(RolePermission)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-22 13:30:54
 */
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {
    /**
     * 批量新增
     *
     * @param entities 角色-权限对象列表
     */
    void insertBatch(@Param("entities") List<RolePermissionEntity> entities);
}

