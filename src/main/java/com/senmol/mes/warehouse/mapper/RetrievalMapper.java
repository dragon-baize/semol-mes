package com.senmol.mes.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.warehouse.entity.RetrievalEntity;
import org.apache.ibatis.annotations.Param;

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

}

