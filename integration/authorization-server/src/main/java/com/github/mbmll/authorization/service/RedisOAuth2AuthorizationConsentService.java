package com.github.mbmll.authorization.service;

import com.github.mbmll.authorization.properties.SpringAuthorizationServerRedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * @Author xlc
 * @Description
 * @Date 2024/2/6 23:30:35
 */

public class RedisOAuth2AuthorizationConsentService extends JdbcOAuth2AuthorizationConsentService {
    /**
     * 查询时放入Redis中的部分 key
     */
    public static final String OAUTH2_AUTHORIZATION_CONSENT = "oauth2_authorization_consent";
    private RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate;
    private SpringAuthorizationServerRedisProperties springAuthorizationServerRedisProperties;

    public RedisOAuth2AuthorizationConsentService(JdbcOperations jdbcOperations,
                                                  RegisteredClientRepository registeredClientRepository,
                                                  RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate,
                                                  SpringAuthorizationServerRedisProperties springAuthorizationServerRedisProperties) {
        super(jdbcOperations, registeredClientRepository);
        this.redisTemplate = redisTemplate;
        this.springAuthorizationServerRedisProperties = springAuthorizationServerRedisProperties;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        super.save(authorizationConsent);
        if (authorizationConsent != null) {
            save0(authorizationConsent);
        }
    }


    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        super.remove(authorizationConsent);
        if (authorizationConsent != null) {
            redisTemplate.opsForValue().getAndDelete(buildKeyRegisteredClientId(authorizationConsent.getRegisteredClientId()));
        }
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        OAuth2AuthorizationConsent oAuth2AuthorizationConsent = redisTemplate.opsForValue().get(buildKeyRegisteredClientId(registeredClientId));

        return super.findById(registeredClientId, principalName);
    }

    private void save0(OAuth2AuthorizationConsent authorizationConsent) {
        long authorizationConsentTimeout = springAuthorizationServerRedisProperties.getAuthorizationConsentTimeout();
        redisTemplate.opsForValue().set(buildKeyRegisteredClientId(authorizationConsent.getRegisteredClientId()),
                authorizationConsent, authorizationConsentTimeout, TimeUnit.SECONDS);
    }

    private String buildKeyRegisteredClientId(String registeredClientId) {
        String prefix = springAuthorizationServerRedisProperties.getPrefix();
        return new StringJoiner(
                SpringAuthorizationServerRedisProperties.REIDS_KEY_DELIMITER)
                .add(prefix).add(OAUTH2_AUTHORIZATION_CONSENT)
                .add(registeredClientId)
                .toString();
    }
}
