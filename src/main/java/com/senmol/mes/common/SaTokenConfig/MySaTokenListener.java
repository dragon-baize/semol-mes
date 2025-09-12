package com.senmol.mes.common.SaTokenConfig;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.senmol.mes.system.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义侦听器的实现
 *
 * @author Administrator
 */
@Component
@Slf4j
public class MySaTokenListener implements SaTokenListener {

    @Resource
    private SaTokenDao saTokenDao;
    @Resource
    private PermissionService permissionService;
    @Value("${login.type.station:login}")
    private String station;

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        log.info("---------- 自定义登录侦听器实现 doLogin");
        String tag = StpUtil.getTokenInfo().getTag();
        // 加载用户权限到缓存
        if (station.equals(tag)) {
            this.permissionService.getByStationId(Long.parseLong(loginId.toString()));
        } else {
            this.permissionService.getByUserId(Long.parseLong(loginId.toString()));
        }
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        log.info("---------- 自定义注销侦听器实现 doLogout");
        // 删除权限缓存
        this.saTokenDao.deleteObject(SaSession.PERMISSION_LIST + ":" + loginId);
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        log.info("---------- 自定义踢下线侦听器实现 doKickout");
        // 删除token缓存
        // String tokenName = StpUtil.getTokenName();
        // this.saTokenDao.deleteObject(tokenName + ":" + loginType + ":token:" + tokenValue);
        // 删除权限缓存
        // this.saTokenDao.deleteObject(SaSession.PERMISSION_LIST + ":" + loginId);
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        log.info("---------- 自定义顶下线侦听器实现 doReplaced");
        // 删除token缓存
        String tokenName = StpUtil.getTokenName();
        this.saTokenDao.deleteObject(tokenName + ":" + loginType + ":token:" + tokenValue);
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
        log.info("---------- 自定义封禁侦听器实现 doDisable");
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
        log.info("---------- 自定义解封侦听器实现 doUntieDisable");
    }

    /**
     * 每次二级认证时触发
     */
    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
        log.info("---------- 自定义二级认证侦听器实现 doOpenSafe");
    }

    /**
     * 每次退出二级认证时触发
     */
    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
        log.info("---------- 自定义退出二级认证侦听器实现 doCloseSafe");
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
        log.info("---------- 自定义创建Session侦听器实现 doCreateSession");
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
        log.info("---------- 自定义注销Session侦听器实现 doLogoutSession");
    }

    /**
     * 每次Token续期时触发
     */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {
        log.info("---------- 自定义Token续期侦听器实现 doRenewTimeout");
    }

}
