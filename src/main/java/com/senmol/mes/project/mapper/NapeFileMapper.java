package com.senmol.mes.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.project.entity.NapeFile;
import com.senmol.mes.project.vo.NapeFileInfo;
import com.senmol.mes.project.vo.NapeFileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 添加项文件(NapeFile)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-21 09:43:40
 */
public interface NapeFileMapper extends BaseMapper<NapeFile> {

    /**
     * 分页查询审核员的审核文件列表
     *
     * @param page     分页对象
     * @param napeFile 实体对象
     * @param userId   人员ID
     * @return 审核文件列表
     */
    List<NapeFileVo> getByUserId(Page<NapeFileVo> page, @Param("file") NapeFile napeFile, @Param("userId") long userId);

    /**
     * 查询审批详情
     *
     * @param id 添加项文件ID
     * @return 审批详情
     */
    NapeFileInfo getOneById(@Param("id") Long id);
}

