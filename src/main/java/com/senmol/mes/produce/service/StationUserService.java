package com.senmol.mes.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.entity.StationUserEntity;

import java.util.List;

/**
 * 工位-人员(StationUser)表服务接口
 *
 * @author makejava
 * @since 2023-01-31 10:18:13
 */
public interface StationUserService extends IService<StationUserEntity> {

    /**
     * 人员查工位、工序
     *
     * @param userId 人员ID
     * @return 工位、工序
     */
    List<ProcessEntity> getByUserId(Long userId);

}

