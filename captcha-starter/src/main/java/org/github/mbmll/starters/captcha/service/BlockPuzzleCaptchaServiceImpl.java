/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package org.github.mbmll.starters.captcha.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.github.mbmll.starters.captcha.CaptchaCache;
import org.github.mbmll.starters.captcha.CaptchaDTO;
import org.github.mbmll.starters.captcha.CaptchaException;
import org.github.mbmll.starters.captcha.PointVO;
import org.github.mbmll.starters.captcha.entity.CachedImage;
import org.github.mbmll.starters.captcha.properties.CaptchaProperties;
import org.github.mbmll.starters.captcha.properties.WaterMark;
import org.github.mbmll.starters.captcha.utils.AESUtil;
import org.github.mbmll.starters.captcha.utils.ImageUtils;
import org.github.mbmll.starters.captcha.utils.RandomUtils;
import org.github.mbmll.starters.captcha.utils.Utils;

import static org.github.mbmll.starters.captcha.Const.HAN_ZI_SIZE;
import static org.github.mbmll.starters.captcha.utils.BlockPuzzleCaptchaUtil.cutByTemplate;
import static org.github.mbmll.starters.captcha.utils.BlockPuzzleCaptchaUtil.interferenceByTemplate;

/**
 * 滑动验证码
 * <p>
 * Created by raodeming on 2019/12/25.
 */
@Slf4j
@Data
public class BlockPuzzleCaptchaServiceImpl implements CaptchaService<CaptchaDTO, Boolean> {

    private Map<String, CachedImage> originalCache = new HashMap<>();
    private Map<String, CachedImage> slidingBlockCache = new HashMap<>();

    private CaptchaProperties captchaProperties;
    private WaterMark waterMark;
    private Font waterMarkFont;

    @Override
    public Boolean verify(CaptchaDTO captchaDTO, CaptchaCache cache) throws Exception {

        //取坐标信息

        byte[] bytes = cache.getAndRemove();
        PointVO cachedPoint = SerializationUtils.deserialize(bytes);
        //aes解密
        String decrypt = AESUtil.aesDecrypt(captchaDTO.getPointJson(), cachedPoint.getSecretKey());
        var mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        PointVO targetPoint = mapper.readValue(Objects.requireNonNull(decrypt).getBytes(StandardCharsets.UTF_8),
            PointVO.class);

        return cachedPoint.getX() - captchaProperties.getSlipOffset() <= targetPoint.getX() &&
            targetPoint.getX() <= cachedPoint.getX() + captchaProperties.getSlipOffset() &&
            Objects.equals(cachedPoint.getY(), targetPoint.getY());
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
        BufferedImage jigsawBufferedImage = ImageUtils.getBufferedImage(cachedJigsawImage);
        if (null == jigsawBufferedImage) {
            throw new CaptchaException("滑动底图未初始化成功，请检查路径");
        }
        cachedJigsawImage.setBufferedImage(jigsawBufferedImage);
        //随机生成拼图坐标
        PointVO point = getRandomPoint(originalImage, jigsawBufferedImage);

        CaptchaDTO captcha = pictureTemplatesCut(originalImage, cachedJigsawImage, point);
        cache.save(SerializationUtils.serialize(captcha.getPoint()));
        if (captcha == null || StringUtils.isBlank(captcha.getJigsawImageBase64()) ||
            StringUtils.isBlank(captcha.getOriginalImageBase64())) {
            throw new CaptchaException("获取验证码失败");
        }

        //point信息不传到前端，只做后端check校验
        captcha.setPoint(null);

        return captcha;
    }

    /**
     * 随机生成拼图坐标.
     *
     * @param originalImage
     * @param jigsawBufferedImage
     * @return
     */
    public PointVO getRandomPoint(BufferedImage originalImage, BufferedImage jigsawBufferedImage) {
        Random random = new Random();
        int widthDifference = originalImage.getWidth() - jigsawBufferedImage.getWidth();
        int heightDifference = originalImage.getHeight() - jigsawBufferedImage.getHeight();
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
    public CaptchaDTO pictureTemplatesCut(BufferedImage originalImage, CachedImage cachedJigsawImage,
        PointVO point) throws IOException {

        CaptchaDTO captchaDTO = new CaptchaDTO();

        int originalWidth = originalImage.getWidth();
        //
        BufferedImage jigsawBufferedImage = cachedJigsawImage.getBufferedImage();
        int jigsawWidth = jigsawBufferedImage.getWidth();
        int jigsawHeight = jigsawBufferedImage.getHeight();

        int x = point.getX();

        //生成新的拼图图像
        BufferedImage newJigsawImage = new BufferedImage(jigsawWidth, jigsawHeight, jigsawBufferedImage.getType());
        Graphics2D graphics = newJigsawImage.createGraphics();

        int bold = 5;
        //如果需要生成RGB格式，需要做如下配置,Transparency 设置透明
        newJigsawImage = graphics.getDeviceConfiguration()
            .createCompatibleImage(jigsawWidth, jigsawHeight, Transparency.TRANSLUCENT);
        // 新建的图像根据模板颜色赋值,源图生成遮罩
        cutByTemplate(originalImage, jigsawBufferedImage, newJigsawImage, x, 0);
        if (captchaProperties.getCaptchaInterferenceOptions() > 0) {
            int position;
            if (originalWidth - x - 5 > jigsawWidth * 2) {
                //在原扣图右边插入干扰图
                position = RandomUtils.getRandomInt(x + jigsawWidth + 5, originalWidth - jigsawWidth);
            } else {
                //在原扣图左边插入干扰图
                position = RandomUtils.getRandomInt(100, x - jigsawWidth - 5);
            }
            CachedImage cachedImage = RandomUtils.pick(slidingBlockCache, cachedJigsawImage.getFileName());
            interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.getBufferedImage(cachedImage)),
                position, 0);
        }
        if (captchaProperties.getCaptchaInterferenceOptions() > 1) {
            CachedImage cachedImage = RandomUtils.pick(slidingBlockCache, cachedJigsawImage.getFileName());
            Integer position = RandomUtils.getRandomInt(jigsawWidth, 100 - jigsawWidth);
            interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.getBufferedImage(cachedImage)),
                position, 0);
        }
        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newJigsawImage, 0, 0, null);
        graphics.dispose();
        // set response body
        captchaDTO.setOriginalImageBase64(ImageUtils.getBase64(originalImage).replaceAll("\r|\n", ""));
        captchaDTO.setJigsawImageBase64(ImageUtils.getBase64(newJigsawImage).replaceAll("\r|\n", ""));
        if (captchaProperties.getCaptchaAesStatus()) {
            point.setSecretKey(AESUtil.getKey());
        }
        captchaDTO.setPoint(point);
        captchaDTO.setToken(RandomUtils.getUUID());
        captchaDTO.setSecretKey(point.getSecretKey());
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
