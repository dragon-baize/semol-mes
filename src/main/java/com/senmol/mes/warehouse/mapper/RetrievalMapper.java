package com.senmol.mes.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.warehouse.entity.RetrievalEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 出库记录(Retrieval)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
public interface RetrievalMapper extends BaseMapper<RetrievalEntity> {

    /**
     * 新增数据
     *
     * @param retrieval 实体对象
     * @return 新增结果
     */
    int saveRetrieval(@Param("entity") RetrievalEntity retrieval);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM warehouse_retrieval WHERE DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

