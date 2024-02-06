package com.github.mbmll.authorization.service;

import com.github.mbmll.authorization.properties.SpringAuthorizationServerRedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @Author xlc
 * @Description
 * @Date 2024/2/6 23:30:35
 */

public class RedisOAuth2AuthorizationConsentService extends JdbcOAuth2AuthorizationConsentService {
    /**
     * 查询时放入Redis中的部分 key
     */
    public static final String OAUTH2_AUTHORIZATION_CONSENT = ":oauth2_authorization_consent:";
    private RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate;
    private SpringAuthorizationServerRedisProperties springAuthorizationServerRedisProperties;

    public RedisOAuth2AuthorizationConsentService(JdbcOperations jdbcOperations,
                                                  RegisteredClientRepository registeredClientRepository,
                                                  RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate) {
        super(jdbcOperations, registeredClientRepository);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        super.save(authorizationConsent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        super.remove(authorizationConsent);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        return super.findById(registeredClientId, principalName);
    }
}
