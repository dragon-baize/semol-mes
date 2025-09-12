package com.senmol.mes.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.CustomProductEntity;

import java.util.List;

/**
 * 客户-产品(CustomProduct)表服务接口
 *
 * @author makejava
 * @since 2023-07-14 15:31:26
 */
public interface CustomProductService extends IService<CustomProductEntity> {

    /**
     * 出库单号查询客户产品
     *
     * @param outBoundCode 出库单号
     * @return 客户产品
     */
    List<CustomProductEntity> getByOutBoundCode(String outBoundCode);

}

