package com.github.mbmll.authorization.client;

import com.github.mbmll.authorization.builder.RedisKeyBuilder;
import com.github.mbmll.authorization.properties.SpringAuthorizationServerRedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import javax.sql.DataSource;

import java.util.concurrent.TimeUnit;

import static com.github.mbmll.authorization.configuration.Constant.REDIS_TEMPLATE_REGISTERED_CLIENT_BEAN_NAME;

/**
 * 只是简单对数据库做了包装,加了 redis 做缓存
 */
@Slf4j
public class RedisRegisteredClientRepository extends JdbcRegisteredClientRepository {

	/**
	 * 根据 id 查询时放入Redis中的部分 key
	 */
	private static final String REGISTERED_CLIENT_ID = "registered_client:id";

	/**
	 * 根据 clientId 查询时放入Redis中的部分 key
	 */
	private static final String REGISTERED_CLIENT_CLIENT_ID = "registered_client:clientId";

	private final RedisTemplate<String, RegisteredClient> redisTemplate;

	private final SpringAuthorizationServerRedisProperties springAuthorizationServerRedisProperties;

	/**
	 * Constructs a {@code JdbcRegisteredClientRepository} using the provided parameters.
	 */
	public RedisRegisteredClientRepository(DataSource dataSource, RedisTemplate<String, RegisteredClient> redisTemplate,
			SpringAuthorizationServerRedisProperties springAuthorizationServerRedisProperties) {
		super(new JdbcTemplate(dataSource));
		this.redisTemplate = redisTemplate;
		this.springAuthorizationServerRedisProperties = springAuthorizationServerRedisProperties;
	}

	@Override
	public void save(RegisteredClient registeredClient) {
		super.save(registeredClient);
		if (registeredClient != null) {
			setRegisteredClient(registeredClient);
		}
	}

	@Override
	public RegisteredClient findById(String id) {
		RegisteredClient registeredClientByRedis = getRegisteredClientById(id);
		if (registeredClientByRedis == null) {
			registeredClientByRedis = super.findById(id);
			log.debug("根据 id：{} 直接查询数据库中的客户：{}", id, registeredClientByRedis);
			if (registeredClientByRedis != null) {
				setRegisteredClient(registeredClientByRedis);
			}
		}
		return registeredClientByRedis;
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		RegisteredClient registeredClient = getRegisteredClientByClientId(clientId);
		if (registeredClient == null) {
			registeredClient = super.findByClientId(clientId);
			log.debug("根据 clientId：{} 直接查询数据库中的客户：{}", clientId, registeredClient);
			if (registeredClient != null) {
				setRegisteredClient(registeredClient);
			}
		}
		return registeredClient;
	}

	public RegisteredClient getRegisteredClientByClientId(String clientId) {
		String prefix = springAuthorizationServerRedisProperties.getPrefix();
		return redisTemplate.opsForValue().get(RedisKeyBuilder.join(prefix, REGISTERED_CLIENT_CLIENT_ID, clientId));
	}

	public RegisteredClient getRegisteredClientById(String id) {
		String prefix = springAuthorizationServerRedisProperties.getPrefix();
		return redisTemplate.opsForValue().get(RedisKeyBuilder.join(prefix, REGISTERED_CLIENT_ID, id));
	}

	public void setRegisteredClient(RegisteredClient registeredClient) {
		long registeredClientTimeout = springAuthorizationServerRedisProperties.getRegisteredClientTimeout();
		String prefix = springAuthorizationServerRedisProperties.getPrefix();
		redisTemplate.opsForValue()
			.set(RedisKeyBuilder.join(prefix, REGISTERED_CLIENT_ID, registeredClient.getId()), registeredClient,
					registeredClientTimeout, TimeUnit.SECONDS);
		redisTemplate.opsForValue()
			.set(RedisKeyBuilder.join(prefix, REGISTERED_CLIENT_CLIENT_ID, registeredClient.getClientId()),
					registeredClient, registeredClientTimeout, TimeUnit.SECONDS);
	}

}
