package com.senmol.mes.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.service.LoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 登录
 *
 * @author Administrator
 */
@Validated
@RestController
@RequestMapping("system")
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 登录
     *
     * @param username 账号
     * @param password 密码
     * @param type     账号标识
     * @return 登录结果
     */
    @Logger("登录")
    @PostMapping("login")
    public SaResult login(@NotBlank(message = "账号不能为空") String username,
                          @NotBlank(message = "密码不能为空") String password,
                          @NotNull(message = "缺少账号标识") Integer type) {
        return this.loginService.login(username, password, type);
    }

    /**
     * 注销
     *
     * @return 注销结果
     */
    @Logger("注销")
    @GetMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("注销成功");
    }

}
