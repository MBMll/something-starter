package com.github.mbmll.authorization.service;

import com.github.mbmll.authorization.properties.SpringAuthorizationServerRedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class RedisOAuth2AuthorizationService extends JdbcOAuth2AuthorizationService {
    /**
     * 根据 id 查询时放入Redis中的部分 key
     */
    public static final String OAUTH2_AUTHORIZATION_ID = ":oauth2_authorization:id:";

    /**
     * 根据 token类型、token 查询时放入Redis中的部分 key
     */
    public static final String OAUTH2_AUTHORIZATION_TOKEN_TYPE = ":oauth2_authorization:tokenType:";
    private RedisTemplate<String, OAuth2Authorization> redisTemplate;
    private SpringAuthorizationServerRedisProperties springAuthorizationServerRedisProperties;

    public RedisOAuth2AuthorizationService(JdbcOperations jdbcOperations,
                                           RegisteredClientRepository registeredClientRepository,
                                           RedisTemplate<String, OAuth2Authorization> redisTemplate,
                                           SpringAuthorizationServerRedisProperties springAuthorizationServerRedisProperties) {
        super(jdbcOperations, registeredClientRepository);
        this.redisTemplate = redisTemplate;
        this.springAuthorizationServerRedisProperties = springAuthorizationServerRedisProperties;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        super.save(authorization);
        if (authorization != null) {
            save0(authorization);
        }
    }


    @Override
    public void remove(OAuth2Authorization authorization) {
        super.remove(authorization);
        if (null != authorization) {
            redisTemplate.opsForValue().getAndDelete(buildKeyOAuth2AuthorizationId(authorization.getId()));
//            todo 
//            redisTemplate.opsForValue().getAndDelete(buildKeyOAuth2AuthorizationToken(authorization.getAuthorizationGrantType()));
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        var oAuth2Authorization = redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationId(id));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }
        return super.findById(id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        var oAuth2Authorization = redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(token,
                tokenType.getValue()));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }
        return super.findByToken(token, tokenType);
    }


    private void save0(OAuth2Authorization authorization) {
        long authorizationTimeout = springAuthorizationServerRedisProperties.getAuthorizationTimeout();
        redisTemplate.opsForValue().set(buildKeyOAuth2AuthorizationId(authorization.getId()), authorization,
                authorizationTimeout, TimeUnit.SECONDS);
    }

    private String buildKeyOAuth2AuthorizationId(String id) {
        String prefix = springAuthorizationServerRedisProperties.getPrefix();
        return new StringJoiner(SpringAuthorizationServerRedisProperties.REIDS_KEY_DELIMITER)
                .add(prefix).add(OAUTH2_AUTHORIZATION_ID).add(id)
                .toString();
    }

    private Object buildKeyOAuth2AuthorizationToken(String token, String tokenType) {
        String prefix = springAuthorizationServerRedisProperties.getPrefix();
        return new StringJoiner(SpringAuthorizationServerRedisProperties.REIDS_KEY_DELIMITER)
                .add(prefix).add(OAUTH2_AUTHORIZATION_TOKEN_TYPE).add(tokenType).add(token)
                .toString();
    }
}
