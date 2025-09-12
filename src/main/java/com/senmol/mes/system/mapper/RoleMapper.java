package com.senmol.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.system.entity.RoleEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 角色(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-22 13:30:54
 */
public interface RoleMapper extends BaseMapper<RoleEntity> {

    int isAdmin(@Param("userId") Long userId);
}

