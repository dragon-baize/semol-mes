package com.senmol.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.system.entity.DictEntity;
import com.senmol.mes.system.vo.DictMxVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 字典(Dict)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-03 17:03:18
 */
public interface DictMapper extends BaseMapper<DictEntity> {
    /**
     * 字典名称查字典明细
     *
     * @param title 字典名称
     * @return 字典明细
     */
    @Select("SELECT b.id, b.title FROM sys_dict a LEFT JOIN sys_dict_mx b ON a.id = b.pid WHERE a.deleted = 0 AND b.deleted = 0 AND a.title = #{title}")
    List<DictMxVo> getMx(@Param("title") String title);
}

