package org.github.mbmll.starters.captcha;

import java.io.IOException;
import org.github.mbmll.starters.captcha.properties.BlockPuzzleCaptchaProperties;
import org.github.mbmll.starters.captcha.properties.CaptchaProperties;
import org.github.mbmll.starters.captcha.properties.WaterMark;
import org.github.mbmll.starters.captcha.service.BlockPuzzleCaptchaDraw;
import org.github.mbmll.starters.captcha.service.BlockPuzzleCaptchaServiceImpl;
import org.github.mbmll.starters.captcha.service.CaptchaService;
import org.github.mbmll.starters.captcha.utils.Utils;
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
    public CaptchaService blockPuzzleCaptchaService(CaptchaProperties captchaProperties,
        BlockPuzzleCaptchaProperties blockPuzzleCaptchaProperties,
        WaterMark waterMark) throws IOException {

        BlockPuzzleCaptchaDraw draw = new BlockPuzzleCaptchaDraw();
        draw.setWaterMarkFont(Utils.loadWaterMarkFont(waterMark.getFont()));
        draw.setCaptchaProperties(captchaProperties);

        BlockPuzzleCaptchaServiceImpl service = new BlockPuzzleCaptchaServiceImpl();
        service.setCaptchaDraw(draw);
        service.setOriginalCache(cacheImage(
            blockPuzzleCaptchaProperties.getBackgroundImagePath(), "defaultImages/jigsaw/original"));
        service.setSlidingBlockCache(cacheImage(
            blockPuzzleCaptchaProperties.getSlidingBlockImagePath(), "defaultImages/jigsaw/slidingBlock"));
        return service;
    }

}
