package com.senmol.mes.system.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.senmol.mes.common.enums.BasicEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.produce.service.StationService;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.service.LoginService;
import com.senmol.mes.system.service.MenuService;
import com.senmol.mes.system.service.UserService;
import com.senmol.mes.system.vo.PageUser;
import com.senmol.mes.system.vo.UserRoute;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MenuService menuService;
    @Resource
    private StationService stationService;

    @Override
    public SaResult login(String username, String password, Integer type) {
        if (type == 3) {
            return this.stationService.login(username, password);
        }

        UserEntity user = this.userService.lambdaQuery().eq(UserEntity::getUsername, username).one();
        if (BeanUtil.isEmpty(user)) {
            return new SaResult(SaResult.CODE_ERROR, ResultEnum.USERNAME_NOT_FOUND_ERROR.getMsg(), -1);
        }

        if (BasicEnum.DISABLE.getCode().equals(user.getStatus())) {
            return new SaResult(SaResult.CODE_ERROR, ResultEnum.ACCOUNT_DISABLED_ERROR.getMsg(), user.getId());
        }

        if (!BasicEnum.CURRENCY.getCode().equals(user.getType()) && !user.getType().equals(type)) {
            return new SaResult(SaResult.CODE_ERROR, ResultEnum.USERNAME_NOT_FOUND_ERROR.getMsg(), user.getId());
        }

        String md5 = SaSecureUtil.md5(password);
        boolean matches = this.passwordEncoder.matches(md5, user.getPassword());
        if (!matches) {
            return new SaResult(SaResult.CODE_ERROR, ResultEnum.ACCOUNT_OR_PASSWORD_ERROR.getMsg(), user.getId());
        }

        Long id = user.getId();
        StpUtil.login(id);
        SaSession session = StpUtil.getSessionByLoginId(id);
        session.set("tag", "login");
        // 封装账号数据返回
        Map<String, Object> map = MapUtil.newHashMap(4);
        // 用户信息
        map.put("userInfo", BeanUtil.copyProperties(user, PageUser.class));
        // 账号token
        map.put("tokenInfo", StpUtil.getTokenInfo());
        // 查询用户菜单
        List<UserRoute> trees = this.menuService.getByUserId(id);
        map.put("menuTreeInfo", trees);
        return SaResult.data(map);
    }
}
