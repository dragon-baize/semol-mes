package com.senmol.mes.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.project.mapper.ManageStaffMapper;
import com.senmol.mes.project.entity.ManageStaff;
import com.senmol.mes.project.service.ManageStaffService;
import org.springframework.stereotype.Service;

/**
 * 项目人员结构(ManageStaff)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 09:20:16
 */
@Service("manageStaffService")
public class ManageStaffServiceImpl extends ServiceImpl<ManageStaffMapper, ManageStaff> implements ManageStaffService {

}

