package org.github.mbmll.starters.captcha;

import java.io.IOException;
import org.github.mbmll.starters.captcha.properties.BlockPuzzleCaptchaProperties;
import org.github.mbmll.starters.captcha.properties.CaptchaProperties;
import org.github.mbmll.starters.captcha.properties.WaterMark;
import org.github.mbmll.starters.captcha.service.BlockPuzzleCaptchaServiceImpl;
import org.github.mbmll.starters.captcha.service.CaptchaService;
import org.github.mbmll.starters.captcha.service.ClickWordCaptchaServiceImpl;
import org.github.mbmll.starters.captcha.utils.Utils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.github.mbmll.starters.captcha.utils.ImageUtils.cacheImage;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/6 22:03
 */
@Configuration
public class CaptchaConfiguration {

    @Bean("blockPuzzleCaptchaService")
    @ConditionalOnProperty(prefix = "captcha.block-puzzle", name = "enabled", havingValue = "true")
    public CaptchaService blockPuzzleCaptchaService(CaptchaProperties captchaProperties,
        BlockPuzzleCaptchaProperties blockPuzzleCaptchaProperties,
        WaterMark waterMark) throws IOException {

        BlockPuzzleCaptchaServiceImpl service = new BlockPuzzleCaptchaServiceImpl();
        service.setWaterMarkFont(Utils.loadWaterMarkFont(waterMark.getFont()));
        service.setCaptchaProperties(captchaProperties);
        service.setOriginalCache(cacheImage(
            blockPuzzleCaptchaProperties.getBackgroundImagePath(), "defaultImages/jigsaw/original"));
        service.setSlidingBlockCache(cacheImage(
            blockPuzzleCaptchaProperties.getSlidingBlockImagePath(), "defaultImages/jigsaw/slidingBlock"));
        return service;
    }

    @Bean("clickWordCaptchaService")
    @ConditionalOnProperty(prefix = "captcha.click-word", name = "enabled", havingValue = "true")
    public CaptchaService clickWordCaptchaService(CaptchaProperties captchaProperties,
        BlockPuzzleCaptchaProperties blockPuzzleCaptchaProperties,
        WaterMark waterMark) throws IOException {

        var service = new ClickWordCaptchaServiceImpl();
        service.setWaterMarkFont(Utils.loadWaterMarkFont(waterMark.getFont()));
        service.loadClickWordFont();
        service.setCaptchaProperties(captchaProperties);
        service.setClickWordCache(cacheImage(
            blockPuzzleCaptchaProperties.getBackgroundImagePath(), "defaultImages/pic-click"));
        return service;
    }

}
