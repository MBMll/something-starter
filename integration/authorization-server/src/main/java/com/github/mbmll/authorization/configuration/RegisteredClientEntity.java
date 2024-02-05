package com.github.mbmll.authorization.configuration;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * @Author xlc
 * @Description
 * @Date 2024/2/5 23:32:35
 */
@Data
public class RegisteredClientEntity implements Serializable {
    private String id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientName;
    private Set<ClientAuthenticationMethodEntity> clientAuthenticationMethods;
    private Set<AuthorizationGrantTypeEntity> authorizationGrantTypes;
    private Set<String> redirectUris;
    private Set<String> postLogoutRedirectUris;
    private Set<String> scopes;
    private ClientSettingsEntiy clientSettings;
    private TokenSettingsEntity tokenSettings;
}

