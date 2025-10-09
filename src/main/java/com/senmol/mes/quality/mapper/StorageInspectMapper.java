package com.senmol.mes.quality.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.quality.entity.StorageInspectEntity;
import com.senmol.mes.quality.vo.StorageInspectVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 入库检测(StorageInspect)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-31 09:45:22
 */
public interface StorageInspectMapper extends BaseMapper<StorageInspectEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param storageInspect 查询实体
     * @return 所有数据
     */
    List<StorageInspectVo> selectAll(Page<StorageInspectVo> page,
                                     @Param("se") StorageInspectEntity storageInspect);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    @Select("SELECT count(*) FROM quality_storage_inspect WHERE DATE(create_time) = #{date} FOR UPDATE")
    int getTodayCount(@Param("date") String date);

}

