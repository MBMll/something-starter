package com.github.mbmll.authorization.service;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

public class RedisOAuth2AuthorizationService extends JdbcOAuth2AuthorizationService {

    public RedisOAuth2AuthorizationService(JdbcOperations jdbcOperations,
                                           RegisteredClientRepository registeredClientRepository) {
        super(jdbcOperations, registeredClientRepository);
    }
}
