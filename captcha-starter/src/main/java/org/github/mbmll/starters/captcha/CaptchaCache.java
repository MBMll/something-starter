package org.github.mbmll.starters.captcha;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/4 22:45
 */

public interface CaptchaCache {

    void save(String key, String value);
}
