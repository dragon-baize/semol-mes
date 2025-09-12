package com.senmol.mes.project.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.project.entity.NapeFile;
import com.senmol.mes.project.vo.NapeFileVo;

import java.util.List;

/**
 * 添加项文件(NapeFile)表服务接口
 *
 * @author makejava
 * @since 2023-03-21 09:43:40
 */
public interface NapeFileService extends IService<NapeFile> {

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param napeFile 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<NapeFile> page, NapeFile napeFile);

    /**
     * 分页查询审核员的审核文件列表
     *
     * @param page     分页对象
     * @param napeFile 实体对象
     * @return 审核文件列表
     */
    SaResult getByUserId(Page<NapeFileVo> page, NapeFile napeFile);

    /**
     * 查询审批详情
     *
     * @param id 添加项文件ID
     * @return 审批详情
     */
    SaResult getOneById(Long id);

    /**
     * 新增数据
     *
     * @param napeFile 实体对象
     * @return 新增结果
     */
    SaResult insertNapeFile(NapeFile napeFile);

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    SaResult deleteNapeFile(List<Long> idList);

}

