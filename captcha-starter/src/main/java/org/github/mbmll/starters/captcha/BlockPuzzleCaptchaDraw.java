/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package org.github.mbmll.starters.captcha;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.util.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.github.mbmll.starters.captcha.Const.HAN_ZI_SIZE;
import static org.github.mbmll.starters.captcha.CaptchaService.getEnOrChLength;

/**
 * 滑动验证码
 * <p>
 * Created by raodeming on 2019/12/25.
 */
@Slf4j
@Setter
public class BlockPuzzleCaptchaDraw {

    private Map<String, CachedImage> originalCache = new HashMap<>();
    private Map<String, CachedImage> slidingBlockCache = new HashMap<>();
    private CaptchaProperties captchaProperties;

    /**
     * 新建的图像根据模板颜色赋值,源图生成遮罩.
     *
     * @param oriImage      原图
     * @param templateImage 模板图
     * @param newImage      新抠出的小图
     * @param x             随机扣取坐标X
     * @param y             随机扣取坐标y
     *
     * @throws Exception
     */
    private static void cutByTemplate(BufferedImage oriImage, BufferedImage templateImage, BufferedImage newImage,
                                      int x, int y) {
        //临时数组遍历用于高斯模糊存周边像素值
        int[][] martrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    newImage.setRGB(i, j, oriImage.getRGB(x + i, y + j));

                    //抠图区域高斯模糊
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(martrix));
                }

                //防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                //描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if ((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0) ||
                        (rgb < 0 && downRgb >= 0)) {
                    newImage.setRGB(i, j, Color.white.getRGB());
                    oriImage.setRGB(x + i, y + j, Color.white.getRGB());
                }
            }
        }
    }

    /**
     * 干扰抠图处理
     *
     * @param oriImage      原图
     * @param templateImage 模板图
     * @param x             随机扣取坐标X
     * @param y             随机扣取坐标y
     *
     * @throws Exception
     */
    private static void interferenceByTemplate(BufferedImage oriImage, BufferedImage templateImage, int x, int y) {
        //临时数组遍历用于高斯模糊存周边像素值
        var martrix = new int[3][3];
        var values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (var i = 0; i < xLength; i++) {
            // 模板图片高度
            for (var j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    //抠图区域高斯模糊
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(martrix));
                }
                //防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                //描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if ((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0) ||
                        (rgb < 0 && downRgb >= 0)) {
                    oriImage.setRGB(x + i, y + j, Color.white.getRGB());
                }
            }
        }

    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        var xStart = x - 1;
        var yStart = y - 1;
        var current = 0;
        for (var i = xStart; i < 3 + xStart; i++) {
            for (int j = yStart; j < 3 + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;
                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current] = img.getRGB(tx, ty);
                current++;

            }
        }
    }

    private static void fillMatrix(int[][] matrix, int[] values) {
        var filled = 0;
        for (var x : matrix) {
            for (var j = 0; j < x.length; j++) {
                x[j] = values[filled];
                filled++;
            }
        }
    }

    private static int avgMatrix(int[][] matrix) {
        var r = 0;
        var g = 0;
        var b = 0;
        for (int[] x : matrix) {
            for (var j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                var c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        var level = 8;
        return new Color(r / level, g / level, b / level).getRGB();
    }

    /**
     * 随机生成拼图坐标.
     *
     * @param originalImage
     * @param jigsawImage
     *
     * @return
     */
    private PointVO getRandomPoint(BufferedImage originalImage, BufferedImage jigsawImage) {
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
        String key = null;
        if (captchaProperties.getCaptchaAesStatus()) {
            key = AESUtil.getKey();
        }
        return new PointVO(x, y, key);
    }

    /**
     * 根据模板切图
     *
     * @throws Exception
     */
    public CaptchaDTO pictureTemplatesCut(BufferedImage originalImage, BufferedImage jigsawImage,
                                          String jigsawImageBase64) throws IOException {
        CaptchaDTO captchaDTO = new CaptchaDTO();

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        //
        int jigsawWidth = jigsawImage.getWidth();
        int jigsawHeight = jigsawImage.getHeight();

        //随机生成拼图坐标
        PointVO point = getRandomPoint(originalImage, jigsawImage);
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
            int position = 0;
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
                    interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.getBase64StrToImage(s)),
                                           position, 0);
                    break;
                }
            }
        }
        if (captchaProperties.getCaptchaInterferenceOptions() > 1) {
            while (true) {
                String s = ImageUtils.getslidingBlock();
                if (!jigsawImageBase64.equals(s)) {
                    Integer randomInt = RandomUtils.getRandomInt(jigsawWidth, 100 - jigsawWidth);
                    interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.getBase64StrToImage(s)),
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
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            //利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            ImageIO.write(newJigsawImage, IMAGE_TYPE_PNG, os);
            jigsawImages = os.toByteArray();
        }
        //新建流。
        try (ByteArrayOutputStream oriImagesOs = new ByteArrayOutputStream()) {
            //利用ImageIO类提供的write方法，将bi以jpg图片的数据模式写入流。
            ImageIO.write(originalImage, IMAGE_TYPE_PNG, oriImagesOs);
            byte[] oriCopyImages = oriImagesOs.toByteArray();
            //新建流。
            Base64.Encoder encoder = Base64.getEncoder();
            captchaDTO.setOriginalImageBase64(encoder.encodeToString(oriCopyImages).replaceAll("\r|\n", ""));
            //point信息不传到前端，只做后端check校验
            //            captchaDTO.setPoint(point);
            captchaDTO.setJigsawImageBase64(encoder.encodeToString(jigsawImages).replaceAll("\r|\n", ""));
            captchaDTO.setToken(RandomUtils.getUUID());
            captchaDTO.setSecretKey(point.getSecretKey());
            //            base64StrToImage(encoder.encodeToString(oriCopyImages), "D:\\原图.png");
            //            base64StrToImage(encoder.encodeToString(jigsawImages), "D:\\滑动.png");
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
        backgroundGraphics.drawString(waterMark, width - getEnOrChLength(waterMark), height - (HAN_ZI_SIZE / 2) + 7);
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
