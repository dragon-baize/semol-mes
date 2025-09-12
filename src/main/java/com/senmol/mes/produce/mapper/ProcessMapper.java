package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.vo.ProcessVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 工序管理(Process)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface ProcessMapper extends BaseMapper<ProcessEntity> {

    /**
     * 工艺查工序
     *
     * @param workmanshipId 工艺ID
     * @param wmsVersion    工艺版本号
     * @return 工序列表
     */
    List<ProcessEntity> getListByWorkmanshipId(@Param("workmanshipId") Long workmanshipId, @Param("wmsVersion") Integer wmsVersion);

    /**
     * 产品查工序
     *
     * @param productId 产品ID
     * @return 工序列表
     */
    List<ProcessEntity> byProductId(@Param("productId") Long productId);

    /**
     * 查询工序
     *
     * @param id 工序ID
     * @return 工序Vo
     */
    @Select("SELECT id, title, workmanship_id, wms_version, take_time, serial_no, station_id FROM produce_process " +
            "WHERE id = #{id}")
    ProcessVo getByIdOrDel(Long id);

}

