/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package org.github.mbmll.starters.captcha;

/**
 * 验证码服务接口
 *
 * @author lide1202@hotmail.com
 * @date 2020-05-12
 */
public interface CaptchaService<T, R> {

    /**
     * 获取验证码
     *
     * @param paramter
     * @return
     */
    R generate(T paramter, CaptchaCache cache);

    /**
     * 二次校验验证码(后端)
     *
     * @param paramter
     * @return
     */
    R verify(T paramter, CaptchaCache cache);

}
