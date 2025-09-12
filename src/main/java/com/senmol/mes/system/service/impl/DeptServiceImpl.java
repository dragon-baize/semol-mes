package com.senmol.mes.system.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.system.entity.DeptEntity;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.mapper.DeptMapper;
import com.senmol.mes.system.service.DeptService;
import com.senmol.mes.system.service.UserService;
import com.senmol.mes.system.vo.DeptTree;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门(Dept)表服务实现类
 *
 * @author makejava
 * @since 2023-01-11 09:01:18
 */
@Service("deptService")
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptEntity> implements DeptService {

    @Resource
    private RedisService redisService;
    @Resource
    private UserService userService;

    @Override
    public List<DeptTree> getList() {
        Object object = this.redisService.get(RedisKeyEnum.SYS_DEPT_TREE.getKey());
        List<DeptTree> trees = Convert.toList(DeptTree.class, object);

        if (CollUtil.isEmpty(trees)) {
            List<DeptEntity> list =
                    this.lambdaQuery().eq(DeptEntity::getStatus, 1).orderByAsc(DeptEntity::getPid).list();
            for (DeptEntity dept : list) {
                if (dept.getPid() == 0L) {
                    trees.add(this.findChildren(dept, list));
                }
            }
        }

        // 缓存部门树
        this.redisService.set(RedisKeyEnum.SYS_DEPT_TREE.getKey(), trees,
                RedisKeyEnum.SYS_DEPT_TREE.getTimeout());
        return trees;
    }

    @Override
    public SaResult insertDept(DeptEntity dept) {
        boolean checkData = this.checkData(dept);
        if (checkData) {
            return SaResult.error("同一级别下的部门名称不能重复");
        }

        // 清空缓存
        this.redisService.del(RedisKeyEnum.SYS_DEPT_TREE.getKey());
        this.save(dept);

        this.redisService.set(RedisKeyEnum.SYS_DEPT.getKey() + dept.getId(), dept.getTitle(),
                RedisKeyEnum.SYS_DEPT.getTimeout());
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateDept(DeptEntity dept) {
        boolean checkData = this.checkData(dept);
        if (checkData) {
            return SaResult.error("同一级别下的部门名称不能重复");
        }

        // 清空缓存
        this.redisService.del(RedisKeyEnum.SYS_DEPT_TREE.getKey());
        this.updateById(dept);

        this.redisService.set(RedisKeyEnum.SYS_DEPT.getKey() + dept.getId(), dept.getTitle(),
                RedisKeyEnum.SYS_DEPT.getTimeout());
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteDept(Long id) {
        DeptEntity dept = this.getById(id);
        if (dept == null) {
            return SaResult.error("删除的数据不存在");
        }

        List<DeptEntity> deptList = this.lambdaQuery().eq(DeptEntity::getPid, id).list();
        if (deptList.size() > 0) {
            return SaResult.error("请先删除子级部门");
        }

        List<UserEntity> list = this.userService.lambdaQuery()
                .eq(UserEntity::getOnJob, 1)
                .eq(UserEntity::getDeptId, id)
                .list();
        if (list.size() > 0) {
            return SaResult.error("部门下存在员工");
        }

        // 清空缓存
        this.redisService.del(RedisKeyEnum.SYS_DEPT_TREE.getKey());
        this.redisService.del(RedisKeyEnum.SYS_DEPT.getKey() + id);
        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private DeptTree findChildren(DeptEntity parent, List<DeptEntity> list) {
        DeptTree vo = Convert.convert(DeptTree.class, parent);
        for (DeptEntity child : list) {
            if (vo.getId().equals(child.getPid())) {
                vo.getChildren().add(findChildren(child, list));
            }
        }

        return vo;
    }

    /**
     * 同一父级下的部门名称不能重复
     */
    private boolean checkData(DeptEntity dept) {
        List<DeptEntity> list = this.lambdaQuery()
                .eq(DeptEntity::getPid, dept.getPid())
                .eq(DeptEntity::getTitle, dept.getTitle())
                .list();
        if (dept.getId() != null) {
            list = list.stream()
                    .filter(item -> !item.getId().equals(dept.getId()))
                    .collect(Collectors.toList());
        }

        return list.size() > 0;
    }

}

