package com.github.mbmll.authorization.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * spring-authorization-server Redis config properties
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.authorization-server.redis")
public class SpringAuthorizationServerRedisProperties {

	public static final String REIDS_KEY_DELIMITER = ":";

	/**
	 * Redis key 前缀
	 */
	private String prefix = "spring-authorization-server";

	/**
	 * registered client Redis 超时时间，单位为秒
	 */
	private long registeredClientTimeout = 300;

	/**
	 * authorization Redis 超时时间，单位为秒
	 */
	private long authorizationTimeout = 300;

	/**
	 * authorization consent Redis 超时时间，单位为秒
	 */
	private long authorizationConsentTimeout = 300;

}
