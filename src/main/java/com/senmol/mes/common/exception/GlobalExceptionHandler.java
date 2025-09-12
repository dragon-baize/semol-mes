package com.senmol.mes.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.senmol.mes.common.enums.SaTokenExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author Administrator
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public SaResult handlerBusinessException(BusinessException e) {
        log.error("业务异常：", e);
        return SaResult.error(e.getMessage());
    }

    /**
     * 单个参数校验异常信息捕获
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public SaResult handlerConstraintViolationException(ConstraintViolationException e) {
        log.error("参数校验异常：", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> list = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return SaResult.error(StrUtil.strip(list.toString(), "[", "]"));
    }

    /**
     * 对象参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public SaResult handlerBindException(BindException e) {
        log.error("对象参数校验异常：", e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        List<String> list = allErrors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return SaResult.error(StrUtil.strip(list.toString(), "[", "]"));
    }

    /**
     * NotLoginException异常
     */
    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerNotLoginException(NotLoginException e) {
        // 打印堆栈，以供调试
        log.error("未登录异常：", e);
        // 返回给前端
        return new SaResult(401, "登录已失效，请重新登录！", null);
    }

    @ExceptionHandler(SaTokenException.class)
    public SaResult handlerSaTokenException(SaTokenException e) {
        log.error("SaToken异常：", e);
        // 根据不同异常细分状态码返回不同的提示
        if (e.getCode() == SaTokenExceptionEnum.REDIRECT_INVALID_ADDR.getCode()) {
            return SaResult.error(SaTokenExceptionEnum.REDIRECT_INVALID_ADDR.getValue());
        }
        if (e.getCode() == SaTokenExceptionEnum.REDIRECT_NOT_IN_ALLOW_RANGE.getCode()) {
            return SaResult.error(SaTokenExceptionEnum.REDIRECT_NOT_IN_ALLOW_RANGE.getValue());
        }
        if (e.getCode() == SaTokenExceptionEnum.INVALID_TICKET.getCode()) {
            return SaResult.error(SaTokenExceptionEnum.INVALID_TICKET.getValue());
        }
        // 更多 code 码判断 ...

        // 默认的提示
        return SaResult.error(SaTokenExceptionEnum.DEFAULT_TIPS.getValue());
    }

    @ExceptionHandler(InterruptedException.class)
    public SaResult handlerInterruptedException(InterruptedException e) {
        log.error("服务中断异常：", e);
        return SaResult.error("服务中断异常，请稍后再试");
    }

    @ExceptionHandler(ExecutionException.class)
    public SaResult handlerExecutionException(ExecutionException e) {
        log.error("服务执行异常：", e);
        return SaResult.error("服务执行异常，请稍后再试");
    }

    /**
     * NotPermissionException异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public SaResult handlerNotPermissionException(NotPermissionException e) {
        // 打印堆栈，以供调试
        log.error("无权限异常：", e);
        // 返回给前端
        return SaResult.error("权限不足");
    }

}
