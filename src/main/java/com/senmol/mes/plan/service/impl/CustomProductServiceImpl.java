package com.senmol.mes.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.plan.mapper.CustomProductMapper;
import com.senmol.mes.plan.entity.CustomProductEntity;
import com.senmol.mes.plan.service.CustomProductService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户-产品(CustomProduct)表服务实现类
 *
 * @author makejava
 * @since 2023-07-14 15:31:26
 */
@Service("customProductService")
public class CustomProductServiceImpl extends ServiceImpl<CustomProductMapper, CustomProductEntity> implements CustomProductService {

    @Override
    public List<CustomProductEntity> getByOutBoundCode(String outBoundCode) {
        return this.baseMapper.getByOutBoundCode(outBoundCode);
    }

}

