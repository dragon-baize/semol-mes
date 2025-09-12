package com.senmol.mes.produce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.produce.entity.PackMaterialEntity;
import com.senmol.mes.produce.mapper.PackMaterialMapper;
import com.senmol.mes.produce.service.PackMaterialService;
import org.springframework.stereotype.Service;

/**
 * 包装-物料(PackMaterial)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Service("packMaterialService")
public class PackMaterialServiceImpl extends ServiceImpl<PackMaterialMapper, PackMaterialEntity> implements PackMaterialService {

}

