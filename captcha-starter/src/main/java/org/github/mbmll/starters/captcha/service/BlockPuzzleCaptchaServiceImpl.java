/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package org.github.mbmll.starters.captcha.service;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.util.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.github.mbmll.starters.captcha.CaptchaCache;
import org.github.mbmll.starters.captcha.CaptchaDTO;
import org.github.mbmll.starters.captcha.CaptchaException;
import org.github.mbmll.starters.captcha.PointVO;
import org.github.mbmll.starters.captcha.ResponseModel;
import org.github.mbmll.starters.captcha.entity.CachedImage;
import org.github.mbmll.starters.captcha.properties.CaptchaProperties;
import org.github.mbmll.starters.captcha.properties.WaterMark;
import org.github.mbmll.starters.captcha.utils.AESUtil;
import org.github.mbmll.starters.captcha.utils.ImageUtils;
import org.github.mbmll.starters.captcha.utils.RandomUtils;
import org.github.mbmll.starters.captcha.utils.Utils;

import static org.github.mbmll.starters.captcha.Const.HAN_ZI_SIZE;
import static org.github.mbmll.starters.captcha.Const.IMAGE_TYPE_PNG;
import static org.github.mbmll.starters.captcha.utils.BlockPuzzleCaptchaUtil.cutByTemplate;
import static org.github.mbmll.starters.captcha.utils.BlockPuzzleCaptchaUtil.interferenceByTemplate;

/**
 * 滑动验证码
 * <p>
 * Created by raodeming on 2019/12/25.
 */
@Slf4j
@Data
public class BlockPuzzleCaptchaServiceImpl implements CaptchaService<CaptchaDTO, CaptchaDTO> {

    private Map<String, CachedImage> originalCache = new HashMap<>();
    private Map<String, CachedImage> slidingBlockCache = new HashMap<>();

    private CaptchaProperties captchaProperties;
    private WaterMark waterMark;
    private Font waterMarkFont;

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
    public CaptchaDTO generate(CaptchaCache cache) throws IOException {
        //原生图片
        BufferedImage originalImage = ImageUtils.getBufferedImage(RandomUtils.pick(originalCache));
        if (null == originalImage) {
            throw new CaptchaException("滑动底图未初始化成功，请检查路径");
        }

        setWaterMark(originalImage);

        //抠图图片
        CachedImage cachedJigsawImage = RandomUtils.pick(slidingBlockCache);
        BufferedImage jigsawImage = ImageUtils.getBufferedImage(cachedJigsawImage);
        if (null == jigsawImage) {
            throw new CaptchaException("滑动底图未初始化成功，请检查路径");
        }

        //随机生成拼图坐标
        PointVO point = getRandomPoint(originalImage, jigsawImage);

        CaptchaDTO captcha = pictureTemplatesCut(originalImage, jigsawImage, cachedJigsawImage.getBase64(), point);
        cache.save(SerializationUtils.serialize(captcha.getPoint()));
        if (captcha == null || StringUtils.isBlank(captcha.getJigsawImageBase64()) ||
            StringUtils.isBlank(captcha.getOriginalImageBase64())) {
            throw new CaptchaException("获取验证码失败");
        }

        //point信息不传到前端，只做后端check校验
        captcha.setPoint(null);

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

    /**
     * 随机生成拼图坐标.
     *
     * @param originalImage
     * @param jigsawImage
     * @return
     */
    public PointVO getRandomPoint(BufferedImage originalImage, BufferedImage jigsawImage) {
        Random random = new Random();
        int widthDifference = originalImage.getWidth() - jigsawImage.getWidth();
        int heightDifference = originalImage.getHeight() - jigsawImage.getHeight();
        int x, y;
        if (widthDifference <= 0) {
            x = 5;
        } else {
            x = random.nextInt(widthDifference - 100) + 100;
        }
        if (heightDifference <= 0) {
            y = 5;
        } else {
            y = random.nextInt(heightDifference) + 5;
        }
        return new PointVO(x, y, null);
    }

    /**
     * 根据模板切图
     *
     * @throws Exception
     */
    public CaptchaDTO pictureTemplatesCut(BufferedImage originalImage, BufferedImage jigsawImage,
        String jigsawImageBase64, PointVO point) throws IOException {

        CaptchaDTO captchaDTO = new CaptchaDTO();

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        //
        int jigsawWidth = jigsawImage.getWidth();
        int jigsawHeight = jigsawImage.getHeight();

        int x = point.getX();
        int y = point.getY();

        //生成新的拼图图像
        BufferedImage newJigsawImage = new BufferedImage(jigsawWidth, jigsawHeight, jigsawImage.getType());
        Graphics2D graphics = newJigsawImage.createGraphics();

        int bold = 5;
        //如果需要生成RGB格式，需要做如下配置,Transparency 设置透明
        newJigsawImage = graphics.getDeviceConfiguration()
            .createCompatibleImage(jigsawWidth, jigsawHeight, Transparency.TRANSLUCENT);
        // 新建的图像根据模板颜色赋值,源图生成遮罩
        cutByTemplate(originalImage, jigsawImage, newJigsawImage, x, 0);
        if (captchaProperties.getCaptchaInterferenceOptions() > 0) {
            int position;
            if (originalWidth - x - 5 > jigsawWidth * 2) {
                //在原扣图右边插入干扰图
                position = RandomUtils.getRandomInt(x + jigsawWidth + 5, originalWidth - jigsawWidth);
            } else {
                //在原扣图左边插入干扰图
                position = RandomUtils.getRandomInt(100, x - jigsawWidth - 5);
            }
            while (true) {
                String s = ImageUtils.getslidingBlock();
                if (!jigsawImageBase64.equals(s)) {
                    interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.getBufferedImage(s)),
                        position, 0);
                    break;
                }
            }
        }
        if (captchaProperties.getCaptchaInterferenceOptions() > 1) {
            RandomUtils.pick(slidingBlockCache, )
            while (true) {
                String s = ImageUtils.getslidingBlock();
                if (!jigsawImageBase64.equals(s)) {
                    Integer randomInt = RandomUtils.getRandomInt(jigsawWidth, 100 - jigsawWidth);
                    interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.getBufferedImage(s)),
                        randomInt, 0);
                    break;
                }
            }
        }

        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newJigsawImage, 0, 0, null);
        graphics.dispose();

        byte[] jigsawImages;
        //新建流。
        try (var os = new ByteArrayOutputStream();
             var oriImagesOs = new ByteArrayOutputStream()) {
            //利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            ImageIO.write(newJigsawImage, IMAGE_TYPE_PNG, os);
            jigsawImages = os.toByteArray();
            //利用ImageIO类提供的write方法，将bi以jpg图片的数据模式写入流。
            ImageIO.write(originalImage, IMAGE_TYPE_PNG, oriImagesOs);
            byte[] oriCopyImages = oriImagesOs.toByteArray();
            //新建流。
            Base64.Encoder encoder = Base64.getEncoder();
            captchaDTO.setOriginalImageBase64(encoder.encodeToString(oriCopyImages).replaceAll("\r|\n", ""));
            if (captchaProperties.getCaptchaAesStatus()) {
                point.setSecretKey(AESUtil.getKey());
            }
            captchaDTO.setPoint(point);
            captchaDTO.setJigsawImageBase64(encoder.encodeToString(jigsawImages).replaceAll("\r|\n", ""));
            captchaDTO.setToken(RandomUtils.getUUID());
            captchaDTO.setSecretKey(point.getSecretKey());
        }

        //将坐标信息存入redis中
        return captchaDTO;
    }

    /**
     * 设置水印
     *
     * @param originalImage
     */
    public void setWaterMark(BufferedImage originalImage) {
        var backgroundGraphics = originalImage.getGraphics();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        backgroundGraphics.setFont(waterMarkFont);
        backgroundGraphics.setColor(Color.white);
        backgroundGraphics.drawString(waterMark.getContent(), width - Utils.getEnOrChLength(waterMark.getContent()), height - (HAN_ZI_SIZE / 2) + 7);
    }

}
