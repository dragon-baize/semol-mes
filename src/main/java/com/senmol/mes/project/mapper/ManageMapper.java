package com.senmol.mes.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.project.entity.Manage;
import com.senmol.mes.project.vo.ManageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目管理(Manage)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
public interface ManageMapper extends BaseMapper<Manage> {
    /**
     * 主键查询审核信息
     *
     * @param id 主键
     * @return 审核信息
     */
    ManageInfo getOneById(@Param("id") Long id);

    /**
     * 分页查询审核员的审核项目列表
     *
     * @param page   分页对象
     * @param manage 实体对象
     * @return 审批项目列表
     */
    List<Manage> getByUserId(Page<Manage> page, @Param("manage") Manage manage, @Param("userId") long userId);
}

