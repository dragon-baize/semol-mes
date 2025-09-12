package com.senmol.mes.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.system.mapper.AsyncMapper;
import com.senmol.mes.system.entity.Async;
import com.senmol.mes.system.service.AsyncService;
import org.springframework.stereotype.Service;

/**
 * 异步异常(Async)表服务实现类
 *
 * @author makejava
 * @since 2024-04-15 09:48:16
 */
@Service("asyncService")
public class AsyncServiceImpl extends ServiceImpl<AsyncMapper, Async> implements AsyncService {

}

