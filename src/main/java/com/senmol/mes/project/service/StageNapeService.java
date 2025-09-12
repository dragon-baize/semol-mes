package com.senmol.mes.project.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.project.entity.StageNape;
import com.senmol.mes.project.vo.StageNapeInfo;

/**
 * 添加项(StageNape)表服务接口
 *
 * @author makejava
 * @since 2023-03-21 09:27:06
 */
public interface StageNapeService extends IService<StageNape> {

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param stageNape 查询实体
     * @return 所有数据
     */
    Page<StageNape> selectAll(Page<StageNape> page, StageNape stageNape);

    /**
     * 主键查询审核信息
     *
     * @param id 主键
     * @return 审核信息
     */
    StageNapeInfo getOneById(Long id);

    /**
     * 分页查询审核员的审核添加项列表
     *
     * @param page      分页对象
     * @param stageNape 实体对象
     * @return 审批项目列表
     */
    Page<StageNape> getByUserId(Page<StageNape> page, StageNape stageNape);

    /**
     * 查询项目添加项树
     *
     * @param manageId 项目ID
     * @return 添加项树
     */
    SaResult getByManageId(Long manageId);

    /**
     * 新增数据
     *
     * @param stageNape 实体对象
     * @return 新增结果
     */
    SaResult insertStageNape(StageNape stageNape);

    /**
     * 修改数据
     *
     * @param stageNape 实体对象
     * @return 修改结果
     */
    SaResult updateStageNape(StageNape stageNape);

    /**
     * 删除数据(已审核的不能删)
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteStageNape(Long id);

}

