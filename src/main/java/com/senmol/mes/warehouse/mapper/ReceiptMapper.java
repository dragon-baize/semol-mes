package com.senmol.mes.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.warehouse.entity.ReceiptEntity;
import com.senmol.mes.warehouse.vo.ReceiptVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收货管理(Receipt)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-13 11:02:36
 */
public interface ReceiptMapper extends BaseMapper<ReceiptEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param receipt 查询实体
     * @return 所有数据
     */
    List<ReceiptVo> selectAll(@Param("page") PageUtil<ReceiptVo> page, @Param("re") ReceiptEntity receipt);

    BigDecimal countPlanQty(@Param("planOrderNo") String planOrderNo);

    void updateStorageQty(@Param("siCode") String siCode,
                          @Param("qty") BigDecimal qty,
                          @Param("now") LocalDateTime now,
                          @Param("userId") Long userId);
}

