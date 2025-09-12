package com.senmol.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.system.entity.MenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-22 13:30:54
 */
public interface MenuMapper extends BaseMapper<MenuEntity> {

    /**
     * 菜单ID列表查父子级菜单
     *
     * @param menuIds 菜单ID列表
     * @return 父子级菜单列表
     */
    List<MenuEntity> getByIds(@Param("menuIds") List<Long> menuIds);

    /**
     * 查询用户菜单
     *
     * @param userIds 用户ID列表
     * @return 用户-菜单列表
     */
    List<CommonPojo> getByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 查询用户-菜单
     *
     * @param userId 用户ID
     * @return 菜单ID列表
     */
    Set<Long> getUserMenuIds(@Param("userId") Long userId);

}

