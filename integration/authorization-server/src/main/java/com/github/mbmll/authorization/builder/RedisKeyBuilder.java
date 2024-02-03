package com.github.mbmll.authorization.builder;

/**
 * @Author xlc
 * @Description
 * @Date 2024/2/3 17:22:42
 */

public class RedisKeyBuilder {

	public static String join(CharSequence... el) {
		return String.join(":", el);
	}

}
