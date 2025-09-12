package com.senmol.mes.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.project.entity.ManageNape;
import com.senmol.mes.project.mapper.ManageNapeMapper;
import com.senmol.mes.project.service.ManageNapeService;
import org.springframework.stereotype.Service;

/**
 * 项目添加项(ManageNape)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 10:23:10
 */
@Service("manageNapeService")
public class ManageNapeServiceImpl extends ServiceImpl<ManageNapeMapper, ManageNape> implements ManageNapeService {

}

