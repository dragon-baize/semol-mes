package com.senmol.mes.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.workflow.entity.FlowUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批人员(FlowUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
public interface FlowUserMapper extends BaseMapper<FlowUser> {

    /**
     * 查询项目审核员
     *
     * @param pid 项目ID
     * @return 审核员列表
     */
    List<FlowUser> getByPid(@Param("pid") Long pid);
}

