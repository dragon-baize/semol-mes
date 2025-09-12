package com.senmol.mes.common.enums;

/**
 * SaToken异常状态码
 *
 * @author Administrator
 */
public enum SaTokenExceptionEnum {

    /**
     * redirect 重定向 url 是一个无效地址
     */
    REDIRECT_INVALID_ADDR(20001, "redirect 重定向 url 是一个无效地址"),
    /**
     * redirect 重定向 url 不在 allowUrl 允许的范围内
     */
    REDIRECT_NOT_IN_ALLOW_RANGE(20002, "redirect 重定向 url 不在 allowUrl 允许的范围内"),
    /**
     * 提供的 ticket 是无效的
     */
    INVALID_TICKET(20004, "提供的 ticket 是无效的"),
    /**
     * 其他异常
     */
    DEFAULT_TIPS(null, "服务器繁忙，请稍后重试...");

    private final Integer code;
    private final String value;

    SaTokenExceptionEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
