package com.senmol.mes.common.SaTokenConfig;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.router.SaRouterStaff;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import com.senmol.mes.common.enums.PermEnum;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.system.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * Sa-Token 权限认证 配置类
 *
 * @author Administrator
 */
@Configuration
@Slf4j
public class SaTokenConfigure implements WebMvcConfigurer {

    @Value("${satoken.open.apis}")
    private String[] openApis;
    @Resource
    private PermissionService permissionService;
    @Resource
    private SaTokenDao saTokenDao;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 设置允许跨域请求的域名
        config.addAllowedOrigin(CorsConfiguration.ALL);
        // 设置允许的方法
        config.addAllowedMethod(HttpMethod.OPTIONS);
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        // 允许任何头
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 登录校验 -- 拦截所有路由，并排除无需登录的开发接口
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());
                    this.checkPermission();
                }))
                .addPathPatterns("/**")
                .excludePathPatterns(this.openApis);
    }

    /**
     * 权限校验
     */
    private void checkPermission() {
        // 获取当前请求类型，请求地址
        SaRequest request = SaHolder.getRequest();
        String method = request.getMethod();
        final String[] path = {request.getRequestPath()};
        log.info("SaTokenConfigure——请求类型：{}，请求地址：{}", method, path);

        List<String> permissionList = StpUtil.getPermissionList(StpUtil.getLoginId());
        if (CollUtil.isEmpty(permissionList)) {
            throw new BusinessException("无此权限");
        }

        final boolean[] notHit = {true};
        // 对请求类型和路径进行权限校验
        List<Dict> list = this.getInnerApi();
        for (Dict dict : list) {
            SaRouterStaff check = SaRouter.match(SaHttpMethod.toEnum(dict.getStr(PermEnum.method.name())))
                    .match(dict.getStr(PermEnum.path.name()))
                    .match(dict.getInt(PermEnum.status.name()) == 1)
                    .check(r -> StpUtil.checkPermission(dict.getStr(PermEnum.code.name())));

            // 匹配到就结束
            if (check.isHit) {
                notHit[0] = false;
                break;
            }
        }

        if (notHit[0]) {
            throw new BusinessException("接口地址不存在");
        }
    }

    /**
     * 查询内部接口
     */
    private List<Dict> getInnerApi() {
        Object object = this.saTokenDao.getObject(RedisKeyEnum.SYS_ALL_CLOSE_URI.getKey());
        List<Dict> list = Convert.toList(Dict.class, object);
        if (CollUtil.isEmpty(list)) {
            log.info("SaTokenConfigure——query——getInnerApi");
            list = this.permissionService.getInnerApi();
        }

        return list;
    }

}
