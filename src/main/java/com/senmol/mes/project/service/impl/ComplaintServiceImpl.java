package com.senmol.mes.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.project.mapper.ComplaintMapper;
import com.senmol.mes.project.entity.Complaint;
import com.senmol.mes.project.service.ComplaintService;
import org.springframework.stereotype.Service;

/**
 * 客诉管理(Complaint)表服务实现类
 *
 * @author makejava
 * @since 2023-03-22 15:05:56
 */
@Service("complaintService")
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements ComplaintService {

}

