package com.senmol.mes.system.service;

import cn.dev33.satoken.util.SaResult;

/**
 * @author Administrator
 */
public interface LoginService {
    /**
     * 登录
     *
     * @param username 账号
     * @param password 密码
     * @param type     账号标识
     * @return 登录结果
     */
    SaResult login(String username, String password, Integer type);

}
