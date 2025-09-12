package com.senmol.mes.warehouse.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.warehouse.entity.ReturnedEntity;

import java.util.List;

/**
 * 退库记录(Returned)表服务接口
 *
 * @author makejava
 * @since 2023-08-04 20:14:40
 */
public interface ReturnedService extends IService<ReturnedEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param returned 查询实体
     * @return 所有数据
     */
    Page<ReturnedEntity> selectAll(Page<ReturnedEntity> page, ReturnedEntity returned);

    /**
     * 物料唯一码查询物料数据
     */
    SaResult byQrCode(String qrCode);

    /**
     * 新增数据
     *
     * @param returnedList 实体对象列表
     * @return 新增结果
     */
    SaResult insertReturnedList(List<ReturnedEntity> returnedList);

}

