package com.senmol.mes.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.warehouse.entity.ReturnedEntity;
import com.senmol.mes.warehouse.vo.ReturnedVo;
import org.apache.ibatis.annotations.Param;

/**
 * 退库记录(Returned)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-04 20:14:40
 */
public interface ReturnedMapper extends BaseMapper<ReturnedEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param returned 查询实体
     * @return 所有数据
     */
    Page<ReturnedEntity> selectAll(Page<ReturnedEntity> page, @Param("returned") ReturnedEntity returned);

    ReturnedVo byQrCode(@Param("qrCode") String qrCode);

    ReturnedVo byObCode(@Param("pickOrder") String pickOrder, @Param("materialId") Long materialId);
}

