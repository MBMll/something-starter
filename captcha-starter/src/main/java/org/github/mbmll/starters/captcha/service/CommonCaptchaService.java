package org.github.mbmll.starters.captcha.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import org.github.mbmll.starters.captcha.properties.WaterMark;
import org.github.mbmll.starters.captcha.utils.Utils;

import static org.github.mbmll.starters.captcha.Const.HAN_ZI_SIZE;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/12 0:27
 */

public abstract class CommonCaptchaService<T, R> implements CaptchaService<T, R> {
    private WaterMark waterMark;
    private Font waterMarkFont;

    /**
     * 设置水印
     *
     * @param bufferedImage
     */
    public void setWaterMark(BufferedImage bufferedImage) {
        var backgroundGraphics = bufferedImage.getGraphics();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        backgroundGraphics.setFont(waterMarkFont);
        backgroundGraphics.setColor(Color.white);
        backgroundGraphics.drawString(waterMark.getContent(), width - Utils.getEnOrChLength(waterMark.getContent()), height - (HAN_ZI_SIZE / 2) + 7);
    }
}
