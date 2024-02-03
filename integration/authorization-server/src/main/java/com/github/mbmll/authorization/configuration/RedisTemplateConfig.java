package com.github.mbmll.authorization.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import static com.github.mbmll.authorization.configuration.Constant.REDIS_TEMPLATE_REGISTERED_CLIENT_BEAN_NAME;

/**
 * @Author xlc
 * @Description
 * @Date 2024/2/3 22:18:10
 */
@Configuration
public class RedisTemplateConfig {

    /**
     * 注意：如果要使用注解 {@link Autowired} 管理 {@link RedisTemplate}， 则需要将 {@link RedisTemplate} 的
     * {@link Bean} 缺省泛型
     * @param redisConnectionFactory Redis 连接工厂
     * @return 返回 Redis 模板
     */
    @Bean(REDIS_TEMPLATE_REGISTERED_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = REDIS_TEMPLATE_REGISTERED_CLIENT_BEAN_NAME)
    public RedisTemplate<String, RegisteredClient> redisTemplateRegisteredClient(
            RedisConnectionFactory redisConnectionFactory) {

        // Helper类简化了 Redis 数据访问代码
        RedisTemplate<String, RegisteredClient> template = new RedisTemplate<>();

        // 设置连接工厂。
        template.setConnectionFactory(redisConnectionFactory);

        // 可以使用读写JSON
        Jackson2JsonRedisSerializer<RegisteredClient> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                RegisteredClient.class);

//        jackson2JsonRedisSerializer.setObjectMapper(ObjectMapperUtils.redis());

        // Redis 字符串：键、值序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);

        // Redis Hash：键、值序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();

        return template;
    }

}
