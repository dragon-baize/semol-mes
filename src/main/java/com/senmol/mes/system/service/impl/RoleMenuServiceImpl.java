package com.senmol.mes.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.system.entity.RoleMenuEntity;
import com.senmol.mes.system.mapper.RoleMenuMapper;
import com.senmol.mes.system.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色-菜单(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-02-03 09:06:17
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuEntity> implements RoleMenuService {

    @Override
    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        List<RoleMenuEntity> entities = new ArrayList<>(menuIds.size());
        for (Long menuId : menuIds) {
            RoleMenuEntity entity = new RoleMenuEntity(roleId, menuId);

            entities.add(entity);
        }

        this.baseMapper.insertBatch(entities);
    }
}

