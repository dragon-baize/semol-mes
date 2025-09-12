package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.entity.StationUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工位-人员(StationUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-31 10:18:13
 */
public interface StationUserMapper extends BaseMapper<StationUserEntity> {

    /**
     * 人员查工位、工序
     *
     * @param userId 人员ID
     * @return 工位、工序
     */
    List<ProcessEntity> getByUserId(@Param("userId") Long userId);

}

