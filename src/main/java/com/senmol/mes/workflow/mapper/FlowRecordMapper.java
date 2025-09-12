package com.senmol.mes.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.workflow.entity.FlowRecord;
import com.senmol.mes.workflow.vo.FlowRecordInfo;
import com.senmol.mes.workflow.vo.FlowRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审核记录(FlowRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
public interface FlowRecordMapper extends BaseMapper<FlowRecord> {
    /**
     * 查询流程详细
     *
     * @param id 主键
     * @return 流程详细
     */
    FlowRecordInfo getMxById(@Param("id") Long id);

    /**
     * 分页查询所有数据
     *
     * @param page       分页对象
     * @param flowRecord 查询实体
     * @param userId     审核员ID
     * @return 所有数据
     */
    List<FlowRecordVo> selectAll(Page<FlowRecordVo> page,
                                 @Param("re") FlowRecord flowRecord,
                                 @Param("userId") long userId);

    void updateStatus(@Param("table") Object table, @Param("status") Integer status, @Param("id") Object id);

}

