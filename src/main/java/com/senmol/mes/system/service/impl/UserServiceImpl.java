package com.senmol.mes.system.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.entity.UserRoleEntity;
import com.senmol.mes.system.mapper.UserMapper;
import com.senmol.mes.system.service.UserRoleService;
import com.senmol.mes.system.service.UserService;
import com.senmol.mes.system.utils.SysAsyncUtil;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.system.vo.PageUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户(User)表服务实现类
 *
 * @author makejava
 * @since 2022-11-22 13:32:04
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private SysAsyncUtil sysAsyncUtil;

    @Override
    public SaResult selectAll(Page<PageUser> page, UserEntity user) {
        List<PageUser> list = this.baseMapper.selectAll(page, user);

        list.forEach(item -> {
            item.setLeaderName(this.sysFromRedis.getUser(item.getLeaderId()));
            item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser()));
        });

        page.setRecords(list);
        return SaResult.data(page);
    }

    @Override
    public UserEntity selectOne(Long id) {
        UserEntity user = this.getById(id);
        // 获取用户-角色数据
        List<UserRoleEntity> userRoles
                = this.userRoleService.lambdaQuery().eq(UserRoleEntity::getUserId, id).list();
        List<Long> roleIds = userRoles.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
        // 设置角色ID
        user.setRoleIds(roleIds);
        return user;
    }

    @Override
    public UserEntity getByIdOrDel(Long id) {
        return this.baseMapper.getByIdOrDel(id);
    }

    @Override
    public SaResult insertUser(UserEntity user) {
        String checkData = this.checkData(user);
        if (StrUtil.isNotBlank(checkData)) {
            return SaResult.error(checkData);
        }

        // 角色ID列表
        List<Long> roleIds = user.getRoleIds();

        // 加密用户密码
        if (user.getPassword() != null) {
            String md5 = SaSecureUtil.md5(user.getPassword());
            user.setPassword(this.passwordEncoder.encode(md5));
        }

        this.save(user);
        // 保存用户-角色数据
        if (user.getId() != null && roleIds != null && roleIds.size() > 0) {
            this.userRoleService.saveUserRole(user.getId(), roleIds);
        }

        // 处理用户权限
        this.sysAsyncUtil.dealUserPerms(user, roleIds);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateUser(UserEntity user) {
        String checkData = this.checkData(user);
        if (StrUtil.isNotBlank(checkData)) {
            return SaResult.error(checkData);
        }

        // 角色ID列表
        List<Long> roleIds = user.getRoleIds();
        // 删除用户-角色数据
        this.userRoleService.lambdaUpdate().eq(UserRoleEntity::getUserId, user.getId()).remove();
        // 保存用户-角色数据
        if (roleIds != null && roleIds.size() > 0) {
            this.userRoleService.saveUserRole(user.getId(), roleIds);
        }

        // 更新用户数据
        this.updateById(user);

        // 处理用户权限
        this.sysAsyncUtil.dealUserPerms(user, roleIds);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteUser(Long id) {
        // 删除用户-角色数据
        this.userRoleService.lambdaUpdate().eq(UserRoleEntity::getUserId, id).remove();
        this.removeById(id);

        // 处理用户权限
        this.sysAsyncUtil.delUserPerms(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private String checkData(UserEntity user) {
        List<UserEntity> list = this.list();

        // 账号重复
        boolean usernameMatch = list.stream().anyMatch(item ->
                !item.getId().equals(user.getId()) && item.getUsername().equals(user.getUsername()));

        if (usernameMatch) {
            return "账号重复";
        }

        // 工号重复
        boolean jobNoMatch = list.stream().anyMatch(item ->
                !item.getId().equals(user.getId()) &&
                        item.getJobNo().equals(user.getJobNo()));

        if (jobNoMatch) {
            return "工号重复";
        }

        return null;
    }

}

