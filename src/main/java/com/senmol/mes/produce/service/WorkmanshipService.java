package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.WorkmanshipEntity;
import com.senmol.mes.produce.vo.WorkmanshipPojo;
import com.senmol.mes.produce.vo.WorkmanshipVo;

import java.util.List;

/**
 * 生产工艺(Workmanship)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
public interface WorkmanshipService extends IService<WorkmanshipEntity> {

    /**
     * 查询未绑定产品的工艺
     *
     * @param productLineId 产线ID
     * @return 所有数据
     */
    List<WorkmanshipPojo> getUnboundList(Long productLineId);

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param workmanship 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<WorkmanshipPojo> page, WorkmanshipEntity workmanship);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    SaResult selectOne(Long id);

    /**
     * 主键查指定数据
     *
     * @param id      主键
     * @param version 版本号
     * @return Vo实体
     */
    WorkmanshipVo getVoById(String id, String version);

    /**
     * 新增数据
     *
     * @param pojo 实体对象
     * @return 新增结果
     */
    SaResult insertWorkmanship(WorkmanshipPojo pojo);

    /**
     * 修改数据
     *
     * @param pojo 实体对象
     * @return 修改结果
     */
    SaResult updateWorkmanship(WorkmanshipPojo pojo);

    /**
     * 复制数据
     *
     * @param id 被复制数据的主键
     * @return 复制结果
     */
    SaResult copyWorkmanship(Long id);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteWorkmanship(Long id);

}

