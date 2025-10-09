package com.senmol.mes.quality.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.quality.entity.StorageInspectEntity;
import com.senmol.mes.quality.vo.StorageInspectVo;

/**
 * 入库检测(StorageInspect)表服务接口
 *
 * @author makejava
 * @since 2023-01-31 09:45:22
 */
public interface StorageInspectService extends IService<StorageInspectEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param storageInspect 查询实体
     * @return 所有数据
     */
    Page<StorageInspectVo> selectAll(Page<StorageInspectVo> page, StorageInspectEntity storageInspect);

    /**
     * 当天已生成的code数量
     *
     * @param date 日期
     * @return 数量
     */
    int getTodayCount(String date);

    /**
     * 新增数据
     *
     * @param storageInspect 实体对象
     * @return 新增结果
     */
    SaResult insertStorageInspect(StorageInspectEntity storageInspect);

    /**
     * 修改数据
     *
     * @param storageInspect 实体对象
     * @return 修改结果
     */
    SaResult updateStorageInspect(StorageInspectEntity storageInspect);

    /**
     * 退回
     *
     * @param id 主键
     * @return 结果
     */
    SaResult back(Long id);

}

