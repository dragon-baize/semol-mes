package com.senmol.mes.common.SaTokenConfig;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoRedisJackson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class SaTokenDaoConfig {
    /**
     * Sa-Token 持久层实现 [Redis存储、Jackson序列化]
     */
    @Bean
    public SaTokenDao saTokenDao() {
        return new SaTokenDaoRedisJackson();
    }

}
