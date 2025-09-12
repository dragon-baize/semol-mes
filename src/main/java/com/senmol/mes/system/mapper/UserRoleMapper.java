package com.senmol.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.system.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 用户-角色(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-03 16:50:20
 */
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {

    /**
     * 查询权限对应用户
     *
     * @param permId 权限ID
     * @return 用户ID列表
     */
    Set<Long> getUserIdByPermId(@Param("permId") Long permId);

    /**
     * 查询菜单对应用户
     *
     * @param menuId 菜单ID
     * @return 用户ID列表
     */
    Set<Long> getUserIdByMenuId(@Param("menuId") Long menuId);

    /**
     * 批量保存
     *
     * @param entities 用户-角色列表
     */
    void insertBatch(@Param("entities") List<UserRoleEntity> entities);

}

