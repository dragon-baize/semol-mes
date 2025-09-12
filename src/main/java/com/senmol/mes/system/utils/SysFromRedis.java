package com.senmol.mes.system.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.system.entity.DictMxEntity;
import com.senmol.mes.system.entity.MenuEntity;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.service.DeptService;
import com.senmol.mes.system.service.DictMxService;
import com.senmol.mes.system.service.MenuService;
import com.senmol.mes.system.service.UserService;
import com.senmol.mes.system.vo.MenuVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Component
public class SysFromRedis {

    @Resource
    private RedisService redisService;
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;
    @Resource
    private DictMxService dictMxService;
    @Resource
    private DeptService deptService;

    /**
     * 查询用户
     */
    public String getUser(Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return "";
        }

        Object object = this.redisService.get(RedisKeyEnum.SYS_USER.getKey() + userId);
        if (ObjectUtil.isNull(object)) {
            UserEntity user = this.userService.getByIdOrDel(userId);
            if (ObjUtil.isNotNull(user)) {
                object = user.getRealName();
            } else {
                object = "";
            }
        }

        this.redisService.set(RedisKeyEnum.SYS_USER.getKey() + userId, object,
                RedisKeyEnum.SYS_USER.getTimeout());
        return object.toString();
    }

    /**
     * 查询菜单
     */
    public List<MenuVo> getMenus() {
        Object object = this.redisService.get(RedisKeyEnum.SYS_MENU.getKey());
        List<MenuVo> menus = Convert.toList(MenuVo.class, object);

        if (CollUtil.isEmpty(menus)) {
            List<MenuEntity> list = this.menuService.lambdaQuery().list();
            menus = Convert.toList(MenuVo.class, list);
        }

        this.redisService.set(RedisKeyEnum.SYS_MENU.getKey(), menus);
        return menus.stream()
                .sorted(Comparator.comparing(MenuVo::getPid))
                .sorted(Comparator.comparing(MenuVo::getSort))
                .collect(Collectors.toList());
    }

    /**
     * 查询用户-菜单关联
     */
    public Set<Long> getUserMenuIds(Long userId) {
        Object object = this.redisService.hGet(RedisKeyEnum.SYS_USER_MENUS.getKey(), userId.toString());
        Set<Long> menuIds = Convert.toSet(Long.class, object);

        if (CollUtil.isEmpty(menuIds)) {
            menuIds = this.menuService.getUserMenuIds(userId);
        }

        this.redisService.hSet(RedisKeyEnum.SYS_USER_MENUS.getKey(), userId.toString(), menuIds);
        return menuIds;
    }

    /**
     * 查询字典
     */
    public String getDictMx(Long dictMxId) {
        if (ObjectUtil.isNull(dictMxId)) {
            return "";
        }

        Object object = this.redisService.get(RedisKeyEnum.SYS_DICT_MX.getKey() + dictMxId);

        if (ObjectUtil.isNull(object)) {
            DictMxEntity dictMx = this.dictMxService.getById(dictMxId);
            if (ObjUtil.isNotNull(dictMx)) {
                object = dictMx.getTitle();
            } else {
                object = "";
            }
        }

        this.redisService.set(RedisKeyEnum.SYS_DICT_MX.getKey() + dictMxId, object,
                RedisKeyEnum.SYS_DICT_MX.getTimeout());
        return object.toString();
    }

    /**
     * 查询部门
     */
    public String getDept(Long deptId) {
        if (ObjectUtil.isNull(deptId)) {
            return "";
        }

        Object object = this.redisService.get(RedisKeyEnum.SYS_DEPT.getKey() + deptId);
        if (ObjectUtil.isNull(object)) {
            object = this.deptService.getById(deptId).getTitle();
        }

        this.redisService.set(RedisKeyEnum.SYS_DEPT.getKey() + deptId, object,
                RedisKeyEnum.SYS_DEPT.getTimeout());
        return object.toString();

    }
}
