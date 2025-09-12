package com.senmol.mes.log.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.log.service.AsyncLogService;
import com.senmol.mes.log.utils.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Administrator
 * @Auther: Administrator
 * @Date: 2023/12/20 9:14
 * @Description: 接口日志输出
 */
@Aspect
@Component
public class LoggerAspect {

    private final ThreadLocal<Long> currentTime = new ThreadLocal<>();
    private static final String LOGIN = "登录";

    @Resource
    private AsyncLogService asyncLogService;

    @Around(value = "@annotation(log)")
    public Object doAround(ProceedingJoinPoint pjp, Logger log) throws Throwable {
        Object result;
        final Long[] opUserId = {null};
        if (!LOGIN.equals(log.value())) {
            try {
                opUserId[0] = StpUtil.getLoginIdAsLong();
            } catch (Exception e) {
                return null;
            }
        }

        currentTime.set(System.currentTimeMillis());
        result = pjp.proceed();
        long time = System.currentTimeMillis() - currentTime.get();
        currentTime.remove();

        if (LOGIN.equals(log.value())) {
            SaResult saResult = (SaResult) result;
            opUserId[0] = saResult.getCode() == SaResult.CODE_ERROR ? Long.parseLong(saResult.getData().toString()) : StpUtil.getLoginIdAsLong();
        }

        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        this.asyncLogService.saveLog(IpUtils.getIpAddr(request), "INFO", pjp, time, opUserId[0], log.value(), null);
        return result;
    }

    @AfterThrowing(value = "@annotation(log)", throwing = "e")
    public void doAfterThrowing(JoinPoint point, Logger log, Throwable e) {
        long userId;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception ex) {
            userId = -1;
        }

        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        this.asyncLogService.saveLog(IpUtils.getIpAddr(request), "ERROR", point, null, userId,
                log.value(), e.toString());
    }

}
