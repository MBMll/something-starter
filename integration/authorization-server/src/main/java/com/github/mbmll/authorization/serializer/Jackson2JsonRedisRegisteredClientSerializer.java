package com.github.mbmll.authorization.serializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.JacksonObjectReader;
import org.springframework.data.redis.serializer.JacksonObjectWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * refer to {@link org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository}
 * @Author xlc
 * @Description
 * @Date 2024/2/5 23:35:42
 */

public class Jackson2JsonRedisRegisteredClientSerializer implements RedisSerializer<RegisteredClient> {

    private final JavaType registeredClientType = TypeFactory.defaultInstance().constructType(RegisteredClient.class);
    private final JavaType registeredClientEntityType =
            TypeFactory.defaultInstance().constructType(RegisteredClientEntity.class);
    private final ObjectMapper mapper;

    private final JacksonObjectReader reader;

    private final JacksonObjectWriter writer;

    public Jackson2JsonRedisRegisteredClientSerializer(ObjectMapper mapper) {
        Assert.notNull(mapper, "ObjectMapper must not be null");
        this.mapper = mapper;
        this.reader = JacksonObjectReader.create();
        this.writer = JacksonObjectWriter.create();
    }

    @Override
    public byte[] serialize(RegisteredClient registeredClient) throws SerializationException {
        try {
            return writer.write(this.mapper, registeredClientType);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    public RegisteredClient deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            RegisteredClientEntity e = (RegisteredClientEntity) reader.read(this.mapper, bytes,
                    registeredClientEntityType);

            RegisteredClient.Builder builder = RegisteredClient.withId(e.getId())
                    .clientId(e.getClientId())
                    .clientIdIssuedAt(e.getClientIdIssuedAt())
                    .clientSecret(e.getClientSecret())
                    .clientSecretExpiresAt(e.getClientSecretExpiresAt())
                    .clientName(e.getClientName())
                    .clientAuthenticationMethods((Set<ClientAuthenticationMethod> authenticationMethods) -> {
                        for (ClientAuthenticationMethodEntity entity : e.getClientAuthenticationMethods()) {
                            authenticationMethods.add(new ClientAuthenticationMethod(entity.getValue()));
                        }
                    })
                    .authorizationGrantTypes((Set<AuthorizationGrantType> grantTypes) -> {
                        for (AuthorizationGrantTypeEntity entity : e.getAuthorizationGrantTypes()) {
                            grantTypes.add(new AuthorizationGrantType(entity.getValue()));
                        }
                    })
                    .redirectUris((uris) -> uris.addAll(e.getRedirectUris()))
                    .postLogoutRedirectUris((uris) -> uris.addAll(e.getPostLogoutRedirectUris()))
                    .scopes((scopes) -> scopes.addAll(e.getScopes()));

            builder.clientSettings(ClientSettings.withSettings(e.getClientSettings().getSettings()).build());
            TokenSettings.Builder tokenSettingsBuilder =
                    TokenSettings.withSettings(e.getTokenSettings().getSettings());
            if (e.getTokenSettings().getSettings().containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
                tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
            }
            builder.tokenSettings(tokenSettingsBuilder.build());
            return builder.build();
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Class<?> getTargetType() {
        return RedisSerializer.super.getTargetType();
    }
}
