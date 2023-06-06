package org.github.mbmll.starters.captcha;

import org.github.mbmll.starters.captcha.utils.RandomUtils;
import org.junit.Test;

import static org.github.mbmll.starters.captcha.utils.AESUtil.aesDecrypt;
import static org.github.mbmll.starters.captcha.utils.AESUtil.aesEncrypt;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/6 21:58
 */

public class AESUtilTest {

    /**
     * 测试
     */
    @Test
    public void xx() throws Exception {
        String randomString = RandomUtils.getRandomString(16);
        String content = "hahhahaahhahni";
        System.out.println("加密前：" + content);
        System.out.println("加密密钥和解密密钥：" + randomString);
        String encrypt = aesEncrypt(content, randomString);
        System.out.println("加密后：" + encrypt);
        String decrypt = aesDecrypt(encrypt, randomString);
        System.out.println("解密后：" + decrypt);
    }

}