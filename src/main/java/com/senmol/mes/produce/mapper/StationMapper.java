package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.produce.entity.StationEntity;
import com.senmol.mes.produce.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工位管理(Station)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
public interface StationMapper extends BaseMapper<StationEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param station 查询实体
     * @return 所有数据
     */
    List<StationVo> selectAll(Page<StationVo> page, @Param("station") StationEntity station);

    /**
     * 工序查工位
     *
     * @param processIds 工序ID列表
     * @return 工位列表
     */
    List<MergesInfo> getByProcessIds(@Param("processIds") List<Long> processIds);

    /**
     * 查询用户绑定工位
     *
     * @param userId 用户ID
     * @return 工位信息
     */
    List<StationInfo> getByUserId(@Param("productLineId") Long productLineId, @Param("userId") Long userId);

    List<BomMaterialVo> getStationByProcessId(@Param("processIds") List<Long> processIds);

    /**
     * 工位编号查工位
     *
     * @param code 工位编号
     * @return 工位信息
     */
    List<StationPojo> getByCode(@Param("code") String code);

}

