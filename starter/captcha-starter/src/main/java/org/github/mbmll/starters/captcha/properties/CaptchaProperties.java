package org.github.mbmll.starters.captcha.properties;

import java.awt.Font;
import lombok.Data;
import org.github.mbmll.starters.captcha.Const;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/5 13:51
 */
@Data
public class CaptchaProperties {
    private Boolean captchaAesStatus = true;
    private Integer captchaInterferenceOptions = 0;
    private Integer slipOffset = 5;
    /**
     * 点选文字 字体总个数
     */
    private Integer wordTotalCount = 4;
    private String clickWordFontStr = "NotoSerif-Light.ttf";
    private Integer fontSize = Const.HAN_ZI_SIZE;
    private Integer fontStyle = Font.BOLD;
}
