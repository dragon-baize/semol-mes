package com.senmol.mes.system.utils;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.senmol.mes.common.enums.PermEnum;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.produce.entity.StationEntity;
import com.senmol.mes.produce.service.StationService;
import com.senmol.mes.system.entity.*;
import com.senmol.mes.system.service.*;
import com.senmol.mes.system.vo.MenuVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Component
public class SysAsyncUtil {

    @Resource
    private RedisService redisService;
    @Resource
    private SaTokenDao saTokenDao;
    @Resource
    private RoleMenuService roleMenuService;
    @Lazy
    @Resource
    private PermissionService permissionService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RolePermissionService rolePermissionService;
    @Lazy
    @Resource
    private MenuService menuService;
    @Resource
    private DictMxService dictMxService;
    @Resource
    private SysFromRedis sysFromRedis;
    @Lazy
    @Resource
    private StationService stationService;

    /**
     * 变更用户权限、菜单
     */
    @Async
    public void dealUserPerms(UserEntity user, List<Long> roleIds) {
        String userId = user.getId().toString();
        // 离职的用户删除缓存数据
        if (user.getOnJob() == 0 || user.getStatus() == 0) {
            this.delUserPerms(user.getId());
            return;
        }

        this.redisService.set(RedisKeyEnum.SYS_USER.getKey() + userId, user.getRealName(),
                RedisKeyEnum.SYS_USER.getTimeout());

        if (CollUtil.isEmpty(roleIds)) {
            return;
        }

        // 查询角色绑定的菜单
        Set<Long> roleMenuIds = this.roleMenuService.lambdaQuery()
                .in(RoleMenuEntity::getRoleId, roleIds)
                .list()
                .stream()
                .map(RoleMenuEntity::getMenuId)
                .collect(Collectors.toSet());
        // 查询缓存菜单
        List<MenuVo> list = this.sysFromRedis.getMenus();
        // 过滤禁用菜单
        Set<Long> menuIds = list.stream()
                .filter(item -> item.getDisabled() == 0 && roleMenuIds.contains(item.getId()))
                .map(MenuVo::getId)
                .collect(Collectors.toSet());

        this.redisService.hSet(RedisKeyEnum.SYS_USER_MENUS.getKey(), userId, menuIds);

        // 绑定用户-权限
        List<String> perms = this.permissionService.getCodesByRoleIds(roleIds);
        this.saTokenDao.setObject(SaSession.PERMISSION_LIST + ":" + userId, perms, -1);
    }

    /**
     * 删除用户权限、token、session
     */
    @Async
    public void delUserPerms(Long userId) {
        // 删token
        String tokenValue = StpUtil.getTokenValueByLoginId(userId);
        if (StrUtil.isNotBlank(tokenValue)) {
            String loginType = StpUtil.getLoginType();
            String tokenName = StpUtil.getTokenName();
            this.saTokenDao.deleteObject(tokenName + ":" + loginType + ":token:" + tokenValue);
        }

        // 删session
        String sessionId = StpUtil.getSessionByLoginId(userId).getId();
        if (StrUtil.isNotBlank(sessionId)) {
            this.saTokenDao.deleteSession(sessionId);
        }

        // 删权限
        this.saTokenDao.deleteObject(SaSession.PERMISSION_LIST + ":" + userId);
        // 删缓存
        this.redisService.del(RedisKeyEnum.SYS_USER.getKey() + userId);
        // 删菜单
        this.redisService.hDel(RedisKeyEnum.SYS_USER_MENUS.getKey(), userId.toString());
    }

    /**
     * 变更菜单
     */
    @Async
    public void dealMenu(MenuEntity menu, Integer iou) {
        Object object = this.redisService.get(RedisKeyEnum.SYS_MENU.getKey());
        List<MenuVo> menus = Convert.toList(MenuVo.class, object);

        // 修改
        if (iou > 0) {
            menus.removeIf(item -> item.getId().equals(menu.getId()));
        }

        menus.add(Convert.convert(MenuVo.class, menu));
        this.redisService.set(RedisKeyEnum.SYS_MENU.getKey(), menus);
    }

    /**
     * 删除菜单
     */
    @Async
    public void delMenu(Long menuId) {
        // 获取拥有此菜单的用户
        Set<Long> userIds = this.userRoleService.getUserIdByMenuId(menuId);
        for (Long userId : userIds) {
            Object object = this.redisService.hGet(RedisKeyEnum.SYS_USER_MENUS.getKey(), userId.toString());
            List<Long> menuIds = Convert.toList(Long.class, object);
            menuIds.remove(menuId);

            // 重置缓存
            this.redisService.hSet(RedisKeyEnum.SYS_USER_MENUS.getKey(), userId.toString(), menuIds);
        }

        // 删除角色-菜单数据
        this.roleMenuService.lambdaUpdate().eq(RoleMenuEntity::getMenuId, menuId).remove();

        Object object = this.redisService.get(RedisKeyEnum.SYS_MENU.getKey());
        List<MenuVo> menus = Convert.toList(MenuVo.class, object);
        menus.removeIf(item -> item.getId().equals(menuId));
        this.redisService.set(RedisKeyEnum.SYS_MENU.getKey(), menus);
    }

    /**
     * 变更接口权限
     */
    @Async
    public void dealPerm(PermissionEntity permission, Integer iou) {
        Object object = this.saTokenDao.getObject(RedisKeyEnum.SYS_ALL_CLOSE_URI.getKey());
        List<Dict> perms = Convert.toList(Dict.class, object);

        if (iou > 0) {
            perms.removeIf(item -> item.getLong("id").equals(permission.getId()));
        }

        perms.add(
                Dict.create()
                        .set(PermEnum.id.name(), permission.getId())
                        .set(PermEnum.method.name(), permission.getApiType())
                        .set(PermEnum.path.name(), permission.getApiUri())
                        .set(PermEnum.code.name(), permission.getCode())
                        .set(PermEnum.status.name(), permission.getStatus())
        );

        this.saTokenDao.setObject(RedisKeyEnum.SYS_ALL_CLOSE_URI.getKey(), perms,
                RedisKeyEnum.SYS_ALL_CLOSE_URI.getTimeout());
    }

    /**
     * 删除权限
     */
    @Async
    public void delPerm(Long permId) {
        Object object = this.saTokenDao.getObject(RedisKeyEnum.SYS_ALL_CLOSE_URI.getKey());
        List<Dict> dicts = Convert.toList(Dict.class, object);

        Dict dict = dicts.stream()
                .filter(item -> item.getLong(PermEnum.id.name()).equals(permId))
                .findFirst()
                .orElse(null);

        if (ObjectUtil.isNull(dict)) {
            return;
        }

        String code = dict.getStr(PermEnum.code.name());
        // 获取绑定该权限的用户ID
        Set<Long> userIds = this.userRoleService.getUserIdByPermId(permId);
        for (Long userId : userIds) {
            Object perms = this.saTokenDao.getObject(SaSession.PERMISSION_LIST + ":" + userId);
            List<String> list = Convert.toList(String.class, perms);

            list.removeIf(item -> item.equals(code));
            this.saTokenDao.setObject(SaSession.PERMISSION_LIST + ":" + userId, list, -1);
        }

        dicts.remove(dict);
        this.saTokenDao.setObject(RedisKeyEnum.SYS_ALL_CLOSE_URI.getKey(), dicts,
                RedisKeyEnum.SYS_ALL_CLOSE_URI.getTimeout());

        // 删除角色-权限表数据
        this.rolePermissionService.lambdaUpdate().eq(RolePermissionEntity::getPermissionId, permId).remove();
    }

    /**
     * 变更角色
     */
    @Async
    public void dealRole(Long roleId, Integer uod) {
        // 查询角色对应的用户
        List<Long> userIds = this.userRoleService.lambdaQuery()
                .eq(UserRoleEntity::getRoleId, roleId)
                .list()
                .stream()
                .map(UserRoleEntity::getUserId)
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(userIds)) {
            return;
        }

        // 查询用户权限
        List<CommonPojo> userPerms = this.permissionService.getByUserIds(userIds);
        // 重置用户权限
        for (Long userId : userIds) {
            Set<String> codes = userPerms.stream().filter(item -> item.getId().equals(userId))
                    .map(CommonPojo::getCode)
                    .collect(Collectors.toSet());

            this.saTokenDao.setObject(SaSession.PERMISSION_LIST + ":" + userId, codes, -1);
        }

        // 查询用户菜单
        List<CommonPojo> userMenus = this.menuService.getByUserIds(userIds);
        // 重置用户菜单
        for (Long userId : userIds) {
            Set<Long> menuIds = userMenus.stream()
                    .filter(item -> item.getId().equals(userId))
                    .map(CommonPojo::getCode)
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

            this.redisService.hSet(RedisKeyEnum.SYS_USER_MENUS.getKey(), userId.toString(), menuIds);
        }

        if (uod > 0) {
            // 删除用户-角色数据
            this.userRoleService.lambdaUpdate().eq(UserRoleEntity::getRoleId, roleId).remove();
        }
    }

    /**
     * 变更角色-工位账号
     */
    @Async
    public void dealRole(Long roleId) {
        List<Long> ids = this.stationService.lambdaQuery()
                .select(StationEntity::getId)
                .list()
                .stream()
                .map(StationEntity::getId)
                .collect(Collectors.toList());

        if (CollUtil.isNotEmpty(ids)) {
            List<String> codes = this.permissionService.getByRoleId(roleId);

            for (Long id : ids) {
                this.saTokenDao.setObject(SaSession.PERMISSION_LIST + ":" + id, codes, -1);
            }
        }
    }

    @Async
    public void delDict(Long dictId) {
        List<DictMxEntity> list = this.dictMxService.lambdaQuery().eq(DictMxEntity::getPid, dictId).list();
        for (DictMxEntity dictMx : list) {
            this.redisService.del(RedisKeyEnum.SYS_DICT_MX.getKey() + dictMx.getId());
        }

        // 删除明细表数据
        this.dictMxService.lambdaUpdate().eq(DictMxEntity::getPid, dictId).remove();
    }

    @Async
    public void dealDictMx(DictMxEntity dictMx) {
        this.redisService.set(RedisKeyEnum.SYS_DICT_MX.getKey() + dictMx.getId(), dictMx.getTitle(),
                RedisKeyEnum.SYS_DICT_MX.getTimeout());
    }

    @Async
    public void delDictMx(List<Long> dictMxIds) {
        for (Long dictMxId : dictMxIds) {
            this.redisService.del(RedisKeyEnum.SYS_DICT_MX.getKey() + dictMxId);
        }
    }

}
