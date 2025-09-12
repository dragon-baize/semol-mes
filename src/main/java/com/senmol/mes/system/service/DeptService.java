package com.senmol.mes.system.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.system.entity.DeptEntity;
import com.senmol.mes.system.vo.DeptTree;

import java.util.List;

/**
 * 部门(Dept)表服务接口
 *
 * @author makejava
 * @since 2023-01-11 09:01:18
 */
public interface DeptService extends IService<DeptEntity> {
    /**
     * 查询所有部门
     *
     * @return 所有数据
     */
    List<DeptTree> getList();

    /**
     * 新增数据
     *
     * @param dept 实体对象
     * @return 新增结果
     */
    SaResult insertDept(DeptEntity dept);

    /**
     * 修改数据
     *
     * @param dept 实体对象
     * @return 修改结果
     */
    SaResult updateDept(DeptEntity dept);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteDept(Long id);
}

