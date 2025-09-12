package com.senmol.mes.produce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.produce.entity.WorkmanshipEntity;
import com.senmol.mes.produce.vo.WorkmanshipPojo;
import com.senmol.mes.produce.vo.WorkmanshipVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产工艺(Workmanship)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
public interface WorkmanshipMapper extends BaseMapper<WorkmanshipEntity> {

    /**
     * 查询未绑定产品的工艺
     *
     * @param productLineId 产线ID
     * @return 所有数据
     */
    List<WorkmanshipPojo> getUnboundList(@Param("productLineId") Long productLineId);

    /**
     * 主键查指定数据
     *
     * @param id      主键
     * @param version 版本号
     * @return Vo实体
     */
    WorkmanshipVo getVoById(@Param("id") String id, @Param("version") String version);

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param workmanship 查询实体
     * @return 所有数据
     */
    List<WorkmanshipPojo> selectAll(Page<WorkmanshipPojo> page, @Param("arg") WorkmanshipEntity workmanship);

    /**
     * 主键查指定数据
     *
     * @param id 主键
     * @return Vo实体
     */
    WorkmanshipPojo getPojoById(@Param("id") Long id);

}

