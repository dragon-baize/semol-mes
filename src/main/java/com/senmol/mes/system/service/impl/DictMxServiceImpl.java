package com.senmol.mes.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.system.entity.DictMxEntity;
import com.senmol.mes.system.mapper.DictMxMapper;
import com.senmol.mes.system.service.DictMxService;
import org.springframework.stereotype.Service;

/**
 * 字典明细(DictMx)表服务实现类
 *
 * @author makejava
 * @since 2023-01-03 17:03:18
 */
@Service("dictMxService")
public class DictMxServiceImpl extends ServiceImpl<DictMxMapper, DictMxEntity> implements DictMxService {

}

