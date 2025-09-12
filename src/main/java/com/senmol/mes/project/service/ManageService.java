package com.senmol.mes.project.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.project.entity.Manage;
import com.senmol.mes.project.vo.ManageInfo;

import java.util.List;

/**
 * 项目管理(Manage)表服务接口
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
public interface ManageService extends IService<Manage> {

    /**
     * 分页查询
     *
     * @param page   分页对象
     * @param manage 查询实体
     * @return 所有数据
     */
    Page<Manage> selectAll(Page<Manage> page, Manage manage);

    /**
     * 主键查询
     *
     * @param id 主键
     * @return 实体
     */
    Manage selectOne(Long id);

    /**
     * 分页查询审核员的审核项目列表
     *
     * @param page   分页对象
     * @param manage 实体对象
     * @return 审批项目列表
     */
    Page<Manage> getByUserId(Page<Manage> page, Manage manage);

    /**
     * 主键查询审核信息
     *
     * @param id 主键
     * @return 审核信息
     */
    ManageInfo getOneById(Long id);

    /**
     * 新增数据
     *
     * @param manage 实体对象
     * @return 新增结果
     */
    SaResult insertManage(Manage manage);

    /**
     * 修改数据
     *
     * @param manage 实体对象
     * @return 修改结果
     */
    SaResult updateManage(Manage manage);

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    SaResult deleteManage(List<Long> idList);

}

