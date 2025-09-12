package com.senmol.mes.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.project.entity.StageNape;
import com.senmol.mes.project.vo.NapeTree;
import com.senmol.mes.project.vo.StageNapeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 添加项(StageNape)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-21 09:27:07
 */
public interface StageNapeMapper extends BaseMapper<StageNape> {
    /**
     * 主键查询审核信息
     *
     * @param id 主键
     * @return 审核信息
     */
    StageNapeInfo getOneById(@Param("id") Long id);

    /**
     * 分页查询审核员的审核添加项列表
     *
     * @param page      分页对象
     * @param stageNape 实体对象
     * @param userId    人员ID
     * @return 审批项目列表
     */
    List<StageNape> getByUserId(Page<StageNape> page, @Param("nape") StageNape stageNape, @Param("userId") long userId);

    /**
     * 查询项目添加项列表
     *
     * @param manageId 项目ID
     * @return 添加项列表
     */
    List<NapeTree> getByManageId(@Param("manageId") Long manageId);

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param stageNape 查询实体
     * @param userId    用户ID
     * @return 所有数据
     */
    List<StageNape> selectAll(Page<StageNape> page, @Param("nape") StageNape stageNape, @Param("userId") Long userId);
}

