package com.senmol.mes.system.service;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.system.entity.PermissionEntity;

import java.util.List;

/**
 * 权限(Permission)表服务接口
 *
 * @author makejava
 * @since 2022-11-22 13:32:04
 */
public interface PermissionService extends IService<PermissionEntity> {

    /**
     * 查询工位接口
     *
     * @param stationId 工位ID
     * @return 权限编号列表
     */
    List<String> getByStationId(long stationId);

    /**
     * 查询用户接口
     *
     * @param userId 用户ID
     * @return 权限编号列表
     */
    List<String> getByUserId(Long userId);

    /**
     * 查询用户接口
     *
     * @param userIds 用户ID列表
     * @return 用户ID-权限编号列表
     */
    List<CommonPojo> getByUserIds(List<Long> userIds);

    /**
     * 查询角色接口
     *
     * @param roleId 角色ID
     * @return 权限编号列表
     */
    List<String> getByRoleId(Long roleId);

    /**
     * 查询内部权限数据列表
     *
     * @return 权限数据列表
     */
    List<Dict> getInnerApi();

    /**
     * 查询角色权限
     *
     * @param roleIds 角色ID列表
     * @return 权限编号列表
     */
    List<String> getCodesByRoleIds(List<Long> roleIds);

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    SaResult getList();

    /**
     * 新增权限
     *
     * @param permission 实体对象
     * @return 新增结果
     */
    SaResult insertPermission(PermissionEntity permission);

    /**
     * 修改数据
     *
     * @param permission 实体对象
     * @return 修改结果
     */
    SaResult updatePermission(PermissionEntity permission);

    /**
     * 删除数据(同时删除菜单-权限表、角色-权限表数据)
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deletePermission(Long id);

}

