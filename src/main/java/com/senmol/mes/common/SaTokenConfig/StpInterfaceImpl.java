package com.senmol.mes.common.SaTokenConfig;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.senmol.mes.system.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 *
 * @author Administrator
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private SaTokenDao saTokenDao;
    @Resource
    private PermissionService permissionService;
    @Value("${login.type.station:login}")
    private String station;

    /**
     * 返回此 loginId 拥有的权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        String tag = session.getString("tag");
        // 查询缓存中的用户权限
        Object permissions = this.saTokenDao.getObject(SaSession.PERMISSION_LIST + ":" + loginId);
        List<String> list = Convert.toList(String.class, permissions);
        if (CollUtil.isEmpty(list)) {
            log.info("StpInterfaceImpl——query——getPermissionList");
            if (station.equals(tag)) {
                list = this.permissionService.getByStationId(Long.parseLong(loginId.toString()));
            } else {
                list = this.permissionService.getByUserId(Long.parseLong(loginId.toString()));
            }
        }

        return list;
    }

    /**
     * 返回此 loginId 拥有的角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
