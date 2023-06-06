/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package org.github.mbmll.starters.captcha;

import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.util.*;
import java.awt.Font;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static org.github.mbmll.starters.captcha.Utils.loadWaterMarkFont;

/**
 * Created by raodeming on 2019/12/25.
 */
@Slf4j
public abstract class AbstractCaptchaService implements CaptchaService {

    protected static final String IMAGE_TYPE_PNG = "png";
    protected static int HAN_ZI_SIZE_HALF = Const.HAN_ZI_SIZE / 2;
    //check校验坐标
    protected static String REDIS_CAPTCHA_KEY = "RUNNING:CAPTCHA:%s";
    //后台二次校验坐标
    protected static String REDIS_SECOND_CAPTCHA_KEY = "RUNNING:CAPTCHA:second-%s";
    protected static Long EXPIRESIN_SECONDS = 2 * 60L;
    protected static Long EXPIRESIN_THREE = 3 * 60L;
    protected static String waterMark = "我的水印";
    protected static String waterMarkFontStr = "WenQuanZhengHei.ttf";
    protected static String slipOffset = "5";
    protected static Boolean captchaAesStatus = true;
    protected static String clickWordFontStr = "WenQuanZhengHei.ttf";
    protected static String cacheType = "local";
    protected static int captchaInterferenceOptions = 0;
    protected Font waterMarkFont;//水印字体
    protected Font clickWordFont;//点选文字字体


    //判断应用是否实现了自定义缓存，没有就使用内存
    @Override
    public void init(final Properties config) {
        //初始化底图
        boolean aBoolean = Boolean.parseBoolean(config.getProperty(Const.CAPTCHA_INIT_ORIGINAL));
        if (!aBoolean) {
            ImageUtils.cacheImage(config.getProperty(Const.ORIGINAL_PATH_JIGSAW), config.getProperty(Const.ORIGINAL_PATH_PIC_CLICK));
        }
        waterMark = config.getProperty(Const.CAPTCHA_WATER_MARK, "我的水印");
        slipOffset = config.getProperty(Const.CAPTCHA_SLIP_OFFSET, "5");
        waterMarkFontStr = config.getProperty(Const.CAPTCHA_WATER_FONT, "WenQuanZhengHei.ttf");
        captchaAesStatus = Boolean.parseBoolean(config.getProperty(Const.CAPTCHA_AES_STATUS, "true"));
        clickWordFontStr = config.getProperty(Const.CAPTCHA_FONT_TYPE, "WenQuanZhengHei.ttf");
        //clickWordFontStr = config.getProperty(Const.CAPTCHA_FONT_TYPE, "SourceHanSansCN-Normal.otf");
        cacheType = config.getProperty(Const.CAPTCHA_CACHETYPE, "local");
        captchaInterferenceOptions = Integer.parseInt(config.getProperty(Const.CAPTCHA_INTERFERENCE_OPTIONS, "0"));

        // 部署在linux中，如果没有安装中文字段，水印和点选文字，中文无法显示，
        // 通过加载resources下的font字体解决，无需在linux中安装字体
        loadWaterMarkFont(waterMarkFontStr);
    }

    protected boolean validatedReq(ResponseModel resp) {
        return resp == null || resp.isSuccess();
    }

    protected String getValidateClientId(CaptchaDTO req) {
        // 以服务端获取的客户端标识 做识别标志
        if (StringUtils.isNotEmpty(req.getBrowserInfo())) {
            return MD5Util.md5(req.getBrowserInfo());
        }
        // 以客户端Ui组件id做识别标志
        if (StringUtils.isNotEmpty(req.getClientUid())) {
            return req.getClientUid();
        }
        return null;
    }

    protected void afterValidateFail(CaptchaDTO data) {
        if (limitHandler != null) {
            // 验证失败 分钟内计数
            String fails = String.format(FrequencyLimitHandler.LIMIT_KEY, "FAIL", data.getClientUid());
            CaptchaCacheService cs = getCacheService(cacheType);
            if (!cs.exists(fails)) {
                cs.set(fails, "1", 60);
            }
            cs.increment(fails, 1);
        }
    }

}
