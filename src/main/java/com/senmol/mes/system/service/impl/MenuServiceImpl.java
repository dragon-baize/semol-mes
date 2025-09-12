package com.senmol.mes.system.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.system.entity.MenuEntity;
import com.senmol.mes.system.entity.RoleEntity;
import com.senmol.mes.system.entity.RoleMenuEntity;
import com.senmol.mes.system.mapper.MenuMapper;
import com.senmol.mes.system.service.MenuService;
import com.senmol.mes.system.service.RoleMenuService;
import com.senmol.mes.system.service.RoleService;
import com.senmol.mes.system.utils.SysAsyncUtil;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.system.vo.MenuTree;
import com.senmol.mes.system.vo.MenuVo;
import com.senmol.mes.system.vo.UserRoute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-11-22 13:32:03
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {

    @Resource
    private RedisService redisService;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private SysAsyncUtil sysAsyncUtil;
    @Resource
    private RoleService roleService;
    @Resource
    private RoleMenuService roleMenuService;
    @Value("${login.type.station:station}")
    private String station;

    @Override
    public List<UserRoute> getByUserId(Long userId) {
        // 获取用户拥有的菜单ID
        Set<Long> menuIds = this.sysFromRedis.getUserMenuIds(userId);
        return this.dealUserRoute(menuIds);
    }

    @Override
    public List<UserRoute> getByStationId(Long stationId) {
        RoleEntity role = this.roleService.lambdaQuery().eq(RoleEntity::getCode, station).last(CheckToolUtil.LIMIT).one();
        Set<Long> menuIds = this.roleMenuService.lambdaQuery()
                .eq(RoleMenuEntity::getRoleId, role.getId())
                .list()
                .stream()
                .map(RoleMenuEntity::getMenuId)
                .collect(Collectors.toSet());

        return this.dealUserRoute(menuIds);
    }

    @Override
    public List<CommonPojo> getByUserIds(List<Long> userIds) {
        return this.baseMapper.getByUserIds(userIds);
    }

    @Override
    public Set<Long> getUserMenuIds(Long userId) {
        return this.baseMapper.getUserMenuIds(userId);
    }

    @Override
    public List<MenuTree> getList() {
        Object object = this.redisService.get(RedisKeyEnum.SYS_MENU_TREE.getKey());
        List<MenuTree> trees = Convert.toList(MenuTree.class, object);

        if (CollUtil.isEmpty(trees)) {
            List<MenuVo> menus = this.sysFromRedis.getMenus();

            for (MenuVo menu : menus) {
                if (menu.getPid() == 0L) {
                    trees.add(findChildren(menu, menus));
                }
            }
        }

        // 缓存菜单树
        this.redisService.set(RedisKeyEnum.SYS_MENU_TREE.getKey(), trees,
                RedisKeyEnum.SYS_MENU_TREE.getTimeout());
        return trees;
    }

    @Override
    public SaResult insertMenu(MenuEntity menu) {
        String checkData = this.checkData(menu);
        if (StrUtil.isNotBlank(checkData)) {
            return SaResult.error(checkData);
        }

        this.save(menu);

        // 删除缓存
        this.redisService.del(RedisKeyEnum.SYS_MENU_TREE.getKey());
        this.sysAsyncUtil.dealMenu(menu, 0);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateMenu(MenuEntity menu) {
        String checkData = this.checkData(menu);
        if (StrUtil.isNotBlank(checkData)) {
            return SaResult.error(checkData);
        }

        this.updateById(menu);
        // 删除缓存
        this.redisService.del(RedisKeyEnum.SYS_MENU_TREE.getKey());
        this.sysAsyncUtil.dealMenu(menu, 1);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteMenu(Long id) {
        this.removeById(id);
        // 删除缓存
        this.redisService.del(RedisKeyEnum.SYS_MENU_TREE.getKey());
        this.sysAsyncUtil.delMenu(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    /**
     * 校验菜单名称、路由重复
     */
    private String checkData(MenuEntity menu) {
        List<MenuVo> menus = this.sysFromRedis.getMenus();

        // 校验菜单
        boolean titleMatch;
        if (menu.getId() != null) {
            titleMatch = menus.stream().anyMatch(item ->
                    !item.getId().equals(menu.getId())
                            && item.getPid().equals(menu.getId())
                            && item.getTitle().equals(menu.getTitle())
            );
        } else {
            titleMatch = menus.stream().anyMatch(item ->
                    item.getPid().equals(menu.getId()) && item.getTitle().equals(menu.getTitle())
            );
        }

        if (titleMatch) {
            return "同级菜单名称重复";
        }

        // 校验路由
        boolean routeMatch;
        if (menu.getId() != null) {
            routeMatch = menus.stream().anyMatch(item ->
                    !item.getId().equals(menu.getId()) && item.getPath().equals(menu.getPath()));
        } else {
            routeMatch = menus.stream().anyMatch(item ->
                    item.getPath().equals(menu.getPath()));
        }

        if (routeMatch) {
            return "菜单路径索引重复";
        }

        return null;
    }

    private MenuTree findChildren(MenuVo parent, List<MenuVo> list) {
        MenuTree tree = Convert.convert(MenuTree.class, parent);
        for (MenuVo child : list) {
            if (tree.getId().equals(child.getPid())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }

                tree.getChildren().add(findChildren(child, list));
            }
        }

        return tree;
    }

    private List<UserRoute> dealUserRoute(Set<Long> menuIds) {
        // 获取所有菜单
        List<MenuVo> menus = this.sysFromRedis.getMenus();
        // 删除所有菜单中用户未拥有的菜单
        menus.removeIf(item -> item.getType() == 10 && !menuIds.contains(item.getId()));
        // 获取子级菜单PID
        Set<Long> ids = menus.stream()
                .filter(item -> item.getType() == 10)
                .map(MenuVo::getPid)
                .collect(Collectors.toSet());
        // 移除不在以上PID范围内的数据
        menus.removeIf(item -> item.getType() == 0 && !ids.contains(item.getId()));

        List<UserRoute> trees = new ArrayList<>();
        for (MenuVo menu : menus) {
            if (menu.getPid() == 0L) {
                trees.add(findChildrenInfo(menu, menus));
            }
        }

        return trees;
    }

    private UserRoute findChildrenInfo(MenuVo parent, List<MenuVo> list) {
        UserRoute route = this.setUserRoute(parent);
        for (MenuVo child : list) {
            if (route.getId().equals(child.getPid())) {
                if (route.getChildren() == null) {
                    route.setChildren(new ArrayList<>());
                }

                route.getChildren().add(findChildrenInfo(child, list));
            }
        }

        return route;
    }

    private UserRoute setUserRoute(MenuVo menu) {
        UserRoute route = new UserRoute();
        route.setId(menu.getId());
        route.setPath(menu.getPath());
        route.setHidden(menu.getDisabled() == 1);
        String component = menu.getType() == 0 ? "Layout" : (menu.getType() == 1 ? "ParentView" : menu.getPath());
        route.setComponent(component);
        route.setName(menu.getName());
        Map<String, Object> map = MapUtil.newHashMap(2);
        map.put("title", menu.getTitle());
        map.put("icon", menu.getIcon());
        map.put("cached", menu.getCached() == 1);
        if (menu.getPid() == 0L) {
            map.put("level", "1");
        }

        route.setMeta(map);
        return route;
    }

}

