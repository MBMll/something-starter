package com.github.mbmll.authorization.service;

import com.github.mbmll.authorization.properties.SpringAuthorizationServerRedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Optional;
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
//            redisTemplate.opsForValue().getAndDelete(buildKeyOAuth2AuthorizationToken(authorization
//            .getAuthorizationGrantType()));
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
        if (tokenType == null) {
            // STATE
            OAuth2Authorization oAuth2Authorization =
                    redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(OAuth2ParameterNames.STATE,
                            token));
            if (oAuth2Authorization != null) {
                return oAuth2Authorization;
            }
            // CODE
            oAuth2Authorization =
                    redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(OAuth2ParameterNames.CODE, token));
            if (oAuth2Authorization != null) {
                return oAuth2Authorization;
            }
            // ACCESS_TOKEN
            oAuth2Authorization =
                    redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(OAuth2TokenType.ACCESS_TOKEN.getValue(), token));
            if (oAuth2Authorization != null) {
                return oAuth2Authorization;
            }
            // OidcParameterNames.ID_TOKEN
            oAuth2Authorization =
                    redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(OidcParameterNames.ID_TOKEN,
                            token));
            if (oAuth2Authorization != null) {
                return oAuth2Authorization;
            }
            // OAuth2TokenType.REFRESH_TOKEN
            oAuth2Authorization =
                    redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(OAuth2TokenType.REFRESH_TOKEN.getValue(), token));
            if (oAuth2Authorization != null) {
                return oAuth2Authorization;
            }
            // OAuth2ParameterNames.DEVICE_CODE
            oAuth2Authorization =
                    redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(OAuth2ParameterNames.DEVICE_CODE
                            , token));
            if (oAuth2Authorization != null) {
                return oAuth2Authorization;
            }
            // OAuth2ParameterNames.USER_CODE
            oAuth2Authorization =
                    redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(OAuth2ParameterNames.USER_CODE,
                            token));
            if (oAuth2Authorization != null) {
                return oAuth2Authorization;
            }
        } else {
            var oAuth2Authorization =
                    redisTemplate.opsForValue().get(buildKeyOAuth2AuthorizationToken(tokenType.getValue(), token));
            if (oAuth2Authorization != null) {
                return oAuth2Authorization;
            }
        }
        return super.findByToken(token, tokenType);
    }


    private void save0(OAuth2Authorization authorization) {
        long authorizationTimeout = springAuthorizationServerRedisProperties.getAuthorizationTimeout();
        // save by id
        redisTemplate.opsForValue().set(buildKeyOAuth2AuthorizationId(authorization.getId()), authorization,
                authorizationTimeout, TimeUnit.SECONDS);
        // save by token
        String tokenKey = findToken(authorization);
        if (tokenKey != null) {
            redisTemplate.opsForValue().set(tokenKey, authorization, authorizationTimeout, TimeUnit.SECONDS);
        }
    }

    /**
     * @param authorization
     *
     * @return
     */
    private String findToken(OAuth2Authorization authorization) {
        // STATE
        Object attribute = authorization.getAttribute(OAuth2ParameterNames.STATE);
        if (attribute != null) {
            return buildKeyOAuth2AuthorizationToken(OAuth2ParameterNames.STATE, attribute.toString());
        }
        // CODE
        OAuth2Authorization.Token<OAuth2AuthorizationCode> token =
                authorization.getToken(OAuth2AuthorizationCode.class);
        if (token != null) {
            return buildKeyOAuth2AuthorizationToken(OAuth2ParameterNames.CODE, token.getToken().getTokenValue());
        }
        // ACCESS_TOKEN
        OAuth2Authorization.Token<OAuth2AccessToken> oAuth2AccessToken =
                authorization.getToken(OAuth2AccessToken.class);
        if (oAuth2AccessToken != null) {
            return buildKeyOAuth2AuthorizationToken(OAuth2TokenType.ACCESS_TOKEN.getValue(),
                    oAuth2AccessToken.getToken().getTokenValue());
        }
        // OidcParameterNames.ID_TOKEN
        OAuth2Authorization.Token<OidcIdToken> oidcIdTokenToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdTokenToken != null) {
            return buildKeyOAuth2AuthorizationToken(OidcParameterNames.ID_TOKEN,
                    oidcIdTokenToken.getToken().getTokenValue());
        }
        // OAuth2TokenType.REFRESH_TOKEN
        OAuth2Authorization.Token<OAuth2RefreshToken> oAuth2RefreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        if (oAuth2RefreshToken != null) {
            return buildKeyOAuth2AuthorizationToken(OAuth2TokenType.REFRESH_TOKEN.getValue(),
                    oAuth2RefreshToken.getToken().getTokenValue());
        }
        // OAuth2ParameterNames.DEVICE_CODE
        OAuth2Authorization.Token<OAuth2DeviceCode> oAuth2DeviceCodeToken =
                authorization.getToken(OAuth2DeviceCode.class);
        if (oAuth2DeviceCodeToken != null) {
            return buildKeyOAuth2AuthorizationToken(OAuth2ParameterNames.DEVICE_CODE,
                    oAuth2DeviceCodeToken.getToken().getTokenValue());
        }
        // OAuth2ParameterNames.USER_CODE
        OAuth2Authorization.Token<OAuth2UserCode> oAuth2UserCodeToken = authorization.getToken(OAuth2UserCode.class);
        if (oAuth2UserCodeToken != null) {
            return buildKeyOAuth2AuthorizationToken(OAuth2ParameterNames.USER_CODE,
                    oAuth2UserCodeToken.getToken().getTokenValue());
        }
        return null;
    }

    private String buildKeyOAuth2AuthorizationId(String id) {
        String prefix = springAuthorizationServerRedisProperties.getPrefix();
        return new StringJoiner(SpringAuthorizationServerRedisProperties.REIDS_KEY_DELIMITER)
                .add(prefix).add(OAUTH2_AUTHORIZATION_ID).add(id).toString();
    }

    private String buildKeyOAuth2AuthorizationToken(String tokenType, String token) {
        String prefix = springAuthorizationServerRedisProperties.getPrefix();
        return new StringJoiner(SpringAuthorizationServerRedisProperties.REIDS_KEY_DELIMITER)
                .add(prefix).add(OAUTH2_AUTHORIZATION_TOKEN_TYPE).add(tokenType).add(token).toString();
    }
}
