package org.github.mbmll.starters.captcha;

import lombok.Data;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/5 13:51
 */
@Data
public class CaptchaProperties {
    private Boolean captchaAesStatus = true;
    private Integer captchaInterferenceOptions = 0;
}
