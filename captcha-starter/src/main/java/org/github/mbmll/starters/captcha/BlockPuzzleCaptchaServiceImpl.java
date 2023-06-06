/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package org.github.mbmll.starters.captcha;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 滑动验证码
 * <p>
 * Created by raodeming on 2019/12/25.
 */
@Slf4j
@Data
public class BlockPuzzleCaptchaServiceImpl extends AbstractCaptchaService<CaptchaDTO, String> {

    private BlockPuzzleCaptchaDraw captchaDraw;

    @Override
    public ResponseModel check(CaptchaDTO captchaDTO) {
        //取坐标信息
        String codeKey = String.format(REDIS_CAPTCHA_KEY, captchaDTO.getToken());
        if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
        }
        String s = CaptchaServiceFactory.getCache(cacheType).get(codeKey);
        //验证码只用一次，即刻失效
        CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
        PointVO point = null;
        PointVO point1 = null;
        String pointJson = null;
        try {
            point = SerializationUtils.deserialize(s.getBytes(StandardCharsets.UTF_8));
            //aes解密
            pointJson = decrypt(captchaDTO.getPointJson(), point.getSecretKey());
            point1 = JsonUtil.parseObject(pointJson, PointVO.class);
        } catch (Exception e) {
            log.error("验证码坐标解析失败", e);
            afterValidateFail(captchaDTO);
            return ResponseModel.errorMsg(e.getMessage());
        }
        if (point.x - Integer.parseInt(slipOffset) > point1.x || point1.x > point.x + Integer.parseInt(slipOffset) ||
                point.y != point1.y) {
            afterValidateFail(captchaDTO);
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR);
        }
        //校验成功，将信息存入缓存
        String secretKey = point.getSecretKey();
        String value = null;
        try {
            value = AESUtil.aesEncrypt(captchaDTO.getToken().concat("---").concat(pointJson), secretKey);
        } catch (Exception e) {
            log.error("AES加密失败", e);
            afterValidateFail(captchaDTO);
            return ResponseModel.errorMsg(e.getMessage());
        }
        String secondKey = String.format(REDIS_SECOND_CAPTCHA_KEY, value);
        CaptchaServiceFactory.getCache(cacheType).set(secondKey, captchaDTO.getToken(), EXPIRESIN_THREE);
        captchaDTO.setResult(true);
        captchaDTO.resetClientFlag();
        return ResponseModel.successData(captchaDTO);
    }

    @Override
    public ResponseModel generate(CaptchaDTO captchaDTO, CaptchaCache cache) throws IOException {
        //原生图片
        BufferedImage originalImage = ImageUtils.getBase64StrToImage(RandomUtils.pick(originalCache));
        if (null == originalImage) {
            throw new CaptchaException("滑动底图未初始化成功，请检查路径");
        }
        captchaDraw.setWaterMark(originalImage);

        //抠图图片
        String jigsawImageBase64 = RandomUtils.pick(slidingBlockCache);
        BufferedImage jigsawImage = ImageUtils.getBase64StrToImage(jigsawImageBase64);
        if (null == jigsawImage) {
            throw new CaptchaException("滑动底图未初始化成功，请检查路径");
        }
        CaptchaDTO captcha = captchaDraw.pictureTemplatesCut(originalImage, jigsawImage, jigsawImageBase64);
        cache.save(String.format(REDIS_CAPTCHA_KEY, captcha.getToken()), captcha.getPoint().toString());
        if (captcha == null || StringUtils.isBlank(captcha.getJigsawImageBase64()) ||
                StringUtils.isBlank(captcha.getOriginalImageBase64())) {
            throw new CaptchaException("获取验证码失败");
        }
        return captcha;
    }

    @Override
    public ResponseModel verify(CaptchaDTO captchaDTO) {
        ResponseModel r = super.verify(captchaDTO);
        if (!validatedReq(r)) {
            return r;
        }
        try {
            String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaDTO.getCaptchaVerification());
            if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
                return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
            }
            //二次校验取值后，即刻失效
            CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
        } catch (Exception e) {
            log.error("验证码坐标解析失败", e);
            return ResponseModel.errorMsg(e.getMessage());
        }
        return ResponseModel.success();
    }
}
