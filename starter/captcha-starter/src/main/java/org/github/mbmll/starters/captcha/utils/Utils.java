package org.github.mbmll.starters.captcha.utils;

import java.awt.Font;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.github.mbmll.starters.captcha.Const;

import static org.github.mbmll.starters.captcha.Const.HAN_ZI_SIZE;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/6 13:04
 */
@Slf4j
public class Utils {
    /**
     * 加载resources下的font字体，add by lide1202@hotmail.com
     * 部署在linux中，如果没有安装中文字段，水印和点选文字，中文无法显示，
     * 通过加载resources下的font字体解决，无需在linux中安装字体
     *
     * @param waterMarkFontStr
     * @return
     */
    public static Font loadWaterMarkFont(String waterMarkFontStr) {
        try {
            if (waterMarkFontStr.toLowerCase().endsWith(".ttf") ||
                waterMarkFontStr.toLowerCase().endsWith(".ttc") ||
                waterMarkFontStr.toLowerCase().endsWith(".otf")) {
                return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(
                        Utils.class.getResourceAsStream("/fonts/" + waterMarkFontStr)))
                    .deriveFont(Font.BOLD, HAN_ZI_SIZE >> 1);
            } else {
                return new Font(waterMarkFontStr, Font.BOLD, HAN_ZI_SIZE / 2);
            }
        } catch (Exception e) {
            log.error("load font error:{}", e);
        }
        return null;
    }

    public static int getEnOrChLength(String content) {
        int enCount = 0;
        int chCount = 0;
        for (int i = 0; i < content.length(); i++) {
            int length = String.valueOf(content.charAt(i)).getBytes(StandardCharsets.UTF_8).length;
            if (length > 1) {
                chCount++;
            } else {
                enCount++;
            }
        }
        int chOffset = (Const.HAN_ZI_SIZE / 2) * chCount + 5;
        int enOffset = enCount * 8;
        return chOffset + enOffset;
    }
}
