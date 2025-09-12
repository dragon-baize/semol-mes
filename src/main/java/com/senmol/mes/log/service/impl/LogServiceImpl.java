package com.senmol.mes.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.log.entity.LogEntity;
import com.senmol.mes.log.mapper.LogMapper;
import com.senmol.mes.log.service.LogService;
import org.springframework.stereotype.Service;

/**
 * 日志
 *
 * @author Administrator
 * @since 2023/12/20 11:20
 */
@Service("sysLogServiceImpl")
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements LogService {

}
