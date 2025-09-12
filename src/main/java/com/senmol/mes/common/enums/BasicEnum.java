package com.senmol.mes.common.enums;

/**
 * 基础枚举类
 *
 * @author Administrator
 */
public enum BasicEnum {

    /**
     * 账号标识
     */
    MES(0), PROJECT(1), CURRENCY(2),
    /**
     * 0-禁用，1-启用
     */
    DISABLE(0), ENABLE(1);

    private final Integer code;

    BasicEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
