package com.senmol.mes.system.service.impl;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.PermEnum;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.system.entity.PermissionEntity;
import com.senmol.mes.system.entity.RoleEntity;
import com.senmol.mes.system.mapper.PermissionMapper;
import com.senmol.mes.system.service.PermissionService;
import com.senmol.mes.system.service.RoleService;
import com.senmol.mes.system.utils.SysAsyncUtil;
import com.senmol.mes.system.vo.PermRoleVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 权限(Permission)表服务实现类
 *
 * @author makejava
 * @since 2022-11-22 13:32:04
 */
@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    @Resource
    private SaTokenDao saTokenDao;
    @Lazy
    @Resource
    private SysAsyncUtil sysAsyncUtil;
    @Resource
    private RedisService redisService;
    @Lazy
    @Resource
    private RoleService roleService;
    @Value("${login.type.station:station}")
    private String station;

    @Override
    public List<String> getByStationId(long stationId) {
        Long id = this.roleService.lambdaQuery().eq(RoleEntity::getCode, station).last(CheckToolUtil.LIMIT).one().getId();
        List<String> codes = this.baseMapper.getCodesByRoleIds(Collections.singletonList(id));
        this.saTokenDao.setObject(SaSession.PERMISSION_LIST + ":" + stationId, codes, -1);
        return codes;
    }

    @Override
    public List<String> getByUserId(Long userId) {
        List<String> codes = this.baseMapper.getByUserId(userId);
        this.saTokenDao.setObject(SaSession.PERMISSION_LIST + ":" + userId, codes, -1);
        return codes;
    }

    @Override
    public List<CommonPojo> getByUserIds(List<Long> userIds) {
        return this.baseMapper.getByUserIds(userIds);
    }

    @Override
    public List<String> getByRoleId(Long roleId) {
        return this.baseMapper.getByRoleId(roleId);
    }

    @Override
    public List<Dict> getInnerApi() {
        List<Dict> list = new ArrayList<>();
        // 查询内部接口列表
        List<PermissionEntity> permissions = this.lambdaQuery().list();

        permissions.forEach(permission ->
                list.add(
                        Dict.create()
                                .set(PermEnum.id.name(), permission.getId())
                                .set(PermEnum.method.name(), permission.getApiType())
                                .set(PermEnum.path.name(), permission.getApiUri())
                                .set(PermEnum.code.name(), permission.getCode())
                                .set(PermEnum.status.name(), permission.getStatus())
                )
        );

        // 存入缓存永久保存
        this.saTokenDao.setObject(SaSession.PERMISSION_LIST + ":ALL_CLOSE_URI", list, -1);
        return list;
    }

    @Override
    public List<String> getCodesByRoleIds(List<Long> roleIds) {
        return this.baseMapper.getCodesByRoleIds(roleIds);
    }

    @Override
    public SaResult getList() {
        Object object = this.redisService.get(RedisKeyEnum.SYS_PERMISSION_TREE.getKey());
        if (ObjectUtil.isNotNull(object)) {
            return SaResult.data(Convert.toList(PermRoleVo.class, object));
        }

        // 查询按钮权限数据
        List<PermissionEntity> permissionList = this.lambdaQuery()
                .eq(PermissionEntity::getStatus, 1)
                .list();

        // 获取所有目录
        List<String> catalogues = permissionList.stream()
                .map(PermissionEntity::getCatalogue)
                .distinct()
                .collect(Collectors.toList());

        // 结果存储
        List<PermRoleVo> voList = new ArrayList<>();

        // 创建多线程任务
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3,
                6,
                3L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        CompletionService<PermRoleVo> completionService = new ExecutorCompletionService<>(executor);

        // 执行任务
        for (String catalogue : catalogues) {
            // 每个线程处理一个目录下的数据
            List<PermissionEntity> list = permissionList.stream()
                    .filter(item -> item.getCatalogue().equals(catalogue))
                    .collect(Collectors.toList());

            completionService.submit(() -> this.formatRoleData(catalogue, list));
        }

        // 获取返回结果
        for (int i = 0; i < catalogues.size(); i++) {
            try {
                PermRoleVo permRoleVo = completionService.take().get();
                voList.add(permRoleVo);

                // 终止线程池
                executor.shutdown();
            } catch (InterruptedException | ExecutionException e) {
                throw new BusinessException(e);
            }
        }

        this.redisService.set(RedisKeyEnum.SYS_PERMISSION_TREE.getKey(), voList);
        return SaResult.data(voList);
    }

    @Override
    public SaResult insertPermission(PermissionEntity permission) {
        boolean checkCode = this.checkCode(permission);
        if (checkCode) {
            return SaResult.error("权限编号不能重复");
        }

        boolean checkApi = this.checkApi(permission);
        if (checkApi) {
            return SaResult.error("接口地址重复");
        }

        this.save(permission);
        // 变更缓存中的开放接口
        this.sysAsyncUtil.dealPerm(permission, 0);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updatePermission(PermissionEntity permission) {
        PermissionEntity entity = this.getById(permission.getId());
        if (entity == null) {
            return SaResult.error(ResultEnum.DATA_NOT_EXIST.getMsg());
        }

        boolean checkCode = this.checkCode(permission);
        if (checkCode) {
            return SaResult.error("权限编号不能重复");
        }

        boolean checkApi = this.checkApi(permission);
        if (checkApi) {
            return SaResult.error("接口地址重复");
        }

        this.updateById(permission);
        // 变更缓存中的开放接口
        this.sysAsyncUtil.dealPerm(permission, 1);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deletePermission(Long id) {
        // 删除权限表数据
        this.removeById(id);
        // 删除缓存
        this.sysAsyncUtil.delPerm(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    /**
     * 校验权限编号是否重复
     */
    private boolean checkCode(PermissionEntity permission) {
        List<PermissionEntity> list = this.lambdaQuery()
                .eq(PermissionEntity::getCode, permission.getCode())
                .list();
        if (permission.getId() != null) {
            list = list.stream()
                    .filter(item -> !item.getId().equals(permission.getId()))
                    .collect(Collectors.toList());
        }

        return list.size() > 0;
    }

    /**
     * 校验权限接口是否重复，开放接口地址必须是唯一的
     */
    private boolean checkApi(PermissionEntity permission) {
        List<PermissionEntity> list;
        // 内部接口判断唯一的条件：api_type + api_uri
        list = this.lambdaQuery()
                .eq(PermissionEntity::getApiType, permission.getApiType())
                .eq(PermissionEntity::getApiUri, permission.getApiUri())
                .list();

        // 根据id是否为null判断是新增还是修改
        if (permission.getId() != null) {
            list = list.stream()
                    .filter(item -> !item.getId().equals(permission.getId()))
                    .collect(Collectors.toList());
        }

        return list.size() > 0;
    }

    /**
     * 角色权限数据格式化
     */
    private PermRoleVo formatRoleData(String catalogue, List<PermissionEntity> permissionList) {
        PermRoleVo permRoleVo = new PermRoleVo();
        // 目录设置到标题属性上
        permRoleVo.setId(IdUtil.getSnowflakeNextId());
        permRoleVo.setTitle(catalogue);

        // 获取子菜单
        List<String> pages = permissionList.stream()
                .map(PermissionEntity::getPage)
                .distinct()
                .collect(Collectors.toList());

        // 获取每个菜单的按钮
        for (String page : pages) {
            // 获取页面按钮
            List<PermissionEntity> list =
                    permissionList.stream().filter(item -> item.getPage().equals(page)).collect(Collectors.toList());
            // 转成Vo对象列表
            List<PermRoleVo> voList = Convert.toList(PermRoleVo.class, list);

            // 创建实体保存页面数据
            PermRoleVo vo = new PermRoleVo();
            vo.setId(IdUtil.getSnowflakeNextId());
            vo.setTitle(page);
            vo.setChild(voList);

            // 保存到目录下
            permRoleVo.getChild().add(vo);
        }

        return permRoleVo;
    }

}

