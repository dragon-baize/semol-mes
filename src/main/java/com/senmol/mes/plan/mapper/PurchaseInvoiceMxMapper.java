package com.senmol.mes.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senmol.mes.plan.entity.PurchaseInvoiceMx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (PurchaseInvoiceMx)表数据库访问层
 *
 * @author dragon-xiaobai
 * @since 2025-09-01 17:06:05
 */
public interface PurchaseInvoiceMxMapper extends BaseMapper<PurchaseInvoiceMx> {

    /**
     * 批量修改数量、含税价
     *
     * @param pid      父ID
     * @param entities 对象列表
     */
    void updateBatch(@Param("pid") Long pid, @Param("entities") List<PurchaseInvoiceMx> entities);

}

