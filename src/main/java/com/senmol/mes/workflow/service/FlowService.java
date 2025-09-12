package com.senmol.mes.workflow.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.workflow.entity.Flow;
import com.senmol.mes.workflow.vo.MenuTrees;

import java.util.List;

/**
 * 审批流程(Flow)表服务接口
 *
 * @author makejava
 * @since 2023-03-24 14:25:47
 */
public interface FlowService extends IService<Flow> {

    /**
     * 通过菜单ID查询单条数据
     *
     * @param title 菜单
     * @return 单条数据
     */
    SaResult getByTitle(String title);

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    List<MenuTrees> getList();

    /**
     * 主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    Flow selectOne(Long id);

    /**
     * 新增数据
     *
     * @param flow 实体对象
     * @return 新增结果
     */
    SaResult insertFlow(Flow flow);

    /**
     * 修改数据
     *
     * @param flow 实体对象
     * @return 修改结果
     */
    SaResult updateFlow(Flow flow);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteFlow(Long id);

}

