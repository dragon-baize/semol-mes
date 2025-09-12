package com.senmol.mes.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.system.entity.RolePermissionEntity;
import com.senmol.mes.system.mapper.RolePermissionMapper;
import com.senmol.mes.system.service.RolePermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限(RolePermission)表服务实现类
 *
 * @author makejava
 * @since 2022-11-22 13:32:04
 */
@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

    @Override
    public void saveRolePerms(Long roleId, List<Long> permissionIds) {
        List<RolePermissionEntity> entities = new ArrayList<>(permissionIds.size());
        for (Long permissionId : permissionIds) {
            RolePermissionEntity entity = new RolePermissionEntity();
            entity.setRoleId(roleId);
            entity.setPermissionId(permissionId);

            entities.add(entity);
        }

        this.baseMapper.insertBatch(entities);
    }
}

