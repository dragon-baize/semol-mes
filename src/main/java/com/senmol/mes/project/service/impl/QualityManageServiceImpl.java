package com.senmol.mes.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.project.mapper.QualityManageMapper;
import com.senmol.mes.project.entity.QualityManage;
import com.senmol.mes.project.service.QualityManageService;
import org.springframework.stereotype.Service;

/**
 * 质量管理(QualityManage)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 14:31:19
 */
@Service("qualityManageService")
public class QualityManageServiceImpl extends ServiceImpl<QualityManageMapper, QualityManage> implements QualityManageService {

}

