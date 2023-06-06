package org.github.mbmll.starters.captcha.properties;

import lombok.Data;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/6 13:17
 */
@Data
public class WaterMark {
    private String content = "mbmll.github.com";
    private Integer slipOffset = 5;
    private String font = "WenQuanZhengHei.ttf";
}
