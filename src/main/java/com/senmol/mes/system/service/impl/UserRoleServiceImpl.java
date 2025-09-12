package com.senmol.mes.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.system.entity.UserRoleEntity;
import com.senmol.mes.system.mapper.UserRoleMapper;
import com.senmol.mes.system.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户-角色(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-02-03 16:50:20
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    public Set<Long> getUserIdByPermId(Long permId) {
        return this.baseMapper.getUserIdByPermId(permId);
    }

    @Override
    public Set<Long> getUserIdByMenuId(Long menuId) {
        return this.baseMapper.getUserIdByMenuId(menuId);
    }

    @Override
    public void saveUserRole(Long userId, List<Long> roleIds) {
        List<UserRoleEntity> entities = new ArrayList<>(roleIds.size());
        for (Long roleId : roleIds) {
            UserRoleEntity entity = new UserRoleEntity(userId, roleId);

            entities.add(entity);
        }

        this.baseMapper.insertBatch(entities);
    }
}

