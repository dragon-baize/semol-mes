package com.senmol.mes.warehouse.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.warehouse.entity.ReceiptEntity;
import com.senmol.mes.warehouse.vo.ReceiptVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收货管理(Receipt)表服务接口
 *
 * @author makejava
 * @since 2023-02-13 11:02:36
 */
public interface ReceiptService extends IService<ReceiptEntity> {
    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param receipt 查询实体
     * @return 所有数据
     */
    SaResult selectAll(PageUtil<ReceiptVo> page, ReceiptEntity receipt);

    /**
     * 根据计划编号查询收货数据
     *
     * @param planOrderNo 计划编号
     * @return 收货数据
     */
    SaResult getByPlanOrderNo(String planOrderNo);

    SaResult insertReceipt(ReceiptEntity receipt);

    /**
     * 修改数据
     *
     * @param receipt 实体对象
     * @return 修改结果
     */
    SaResult updateReceipt(ReceiptEntity receipt);

    void updateStorageQty(String siCode, BigDecimal qty, LocalDateTime now, Long userId);

    /**
     * 收货删除
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult delById(Long id);

}

