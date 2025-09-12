package com.senmol.mes.common.enums;

/**
 * @author Administrator
 */

public enum ResultEnum {
    /**
     * 账号不存在
     */
    USERNAME_NOT_FOUND_ERROR("账号不存在"),
    /**
     * 账户被禁用
     */
    ACCOUNT_DISABLED_ERROR("账户被禁用"),
    /**
     * 账号或密码错误
     */
    ACCOUNT_OR_PASSWORD_ERROR("账号或密码错误"),
    /**
     * 保存成功
     */
    INSERT_SUCCESS("保存成功"),
    /**
     * 修改成功
     */
    UPDATE_SUCCESS("修改成功"),
    /**
     * 数据不存在
     */
    DATA_NOT_EXIST("数据不存在"),
    /**
     * 删除成功
     */
    DELETE_SUCCESS("删除成功");

    private final String msg;

    ResultEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
