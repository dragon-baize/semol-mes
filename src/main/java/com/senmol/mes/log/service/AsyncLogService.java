package com.senmol.mes.log.service;

import com.senmol.mes.log.entity.LogEntity;
import org.aspectj.lang.JoinPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 异步调用日志服务
 *
 * @author ruoyi
 */
@Service
public class AsyncLogService {

    @Resource
    private LogService logService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveLog(String ip, String type, JoinPoint point, Long time, Long opUserId,
                        String description, String exceptionMsg) {
        LogEntity log = new LogEntity();
        log.setType(type);
        log.setTime(time);
        log.setOpUserId(opUserId);
        log.setOpTime(LocalDateTime.now());
        log.setDescription(description);
        log.setMethod(point.getSignature().toString());
        log.setParams(Arrays.toString(point.getArgs()));
        log.setExceptionMsg(exceptionMsg);
        log.setIp(ip);
        this.logService.save(log);
    }
}
