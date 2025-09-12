package com.senmol.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.system.entity.PermissionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限(Permission)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-22 13:30:54
 */
public interface PermissionMapper extends BaseMapper<PermissionEntity> {
    /**
     * 查询账号权限接口列表
     *
     * @param userId 用户ID
     * @return 权限接口列表
     */
    List<String> getByUserId(@Param("userId") Long userId);

    /**
     * 查询用户接口
     *
     * @param userIds 用户ID列表
     * @return 用户ID-权限编号列表
     */
    List<CommonPojo> getByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 查询角色接口
     *
     * @param roleId 角色ID
     * @return 权限编号列表
     */
    List<String> getByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询角色权限
     *
     * @param roleIds 角色ID列表
     * @return 权限编号列表
     */
    List<String> getCodesByRoleIds(@Param("roleIds") List<Long> roleIds);

}

