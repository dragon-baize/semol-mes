package com.senmol.mes.system.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.system.entity.MenuEntity;
import com.senmol.mes.system.vo.MenuTree;
import com.senmol.mes.system.vo.UserRoute;

import java.util.List;
import java.util.Set;

/**
 * 菜单(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-11-22 13:32:03
 */
public interface MenuService extends IService<MenuEntity> {
    /**
     * 查询用户菜单
     *
     * @param userId 用户ID
     * @return 菜单树列表
     */
    List<UserRoute> getByUserId(Long userId);

    /**
     * 查询工位菜单
     *
     * @param stationId 工位ID
     * @return 菜单树列表
     */
    List<UserRoute> getByStationId(Long stationId);

    /**
     * 查询用户菜单
     *
     * @param userIds 用户ID列表
     * @return 用户-菜单列表
     */
    List<CommonPojo> getByUserIds(List<Long> userIds);

    /**
     * 查询用户-菜单
     *
     * @param userId 用户ID
     * @return 菜单ID列表
     */
    Set<Long> getUserMenuIds(Long userId);

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    List<MenuTree> getList();

    /**
     * 新增数据
     *
     * @param menu 实体对象
     * @return 新增结果
     */
    SaResult insertMenu(MenuEntity menu);

    /**
     * 修改数据
     *
     * @param menu 实体对象
     * @return 修改结果
     */
    SaResult updateMenu(MenuEntity menu);

    /**
     * 删除菜单
     *
     * @param id 主键
     * @return 结果
     */
    SaResult deleteMenu(Long id);

}

