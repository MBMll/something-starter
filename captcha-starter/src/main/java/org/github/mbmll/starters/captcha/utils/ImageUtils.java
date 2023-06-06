/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package org.github.mbmll.starters.captcha.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.github.mbmll.starters.captcha.entity.CachedImage;
import org.springframework.util.Base64Utils;

@Slf4j
public class ImageUtils {

    public static Map<String, CachedImage> cacheImage(String outPath, String defaultPath) throws IOException {
        if (StringUtils.isBlank(outPath)) {
            return getResourcesImagesFile(defaultPath);
        }
        return getImagesFile(outPath);
        //滑动拼图
//        if (StringUtils.isBlank(captchaOriginalPathJigsaw)) {
//            originalCacheMap.putAll(getResourcesImagesFile("defaultImages/jigsaw/original"));
//            slidingBlockCacheMap.putAll(getResourcesImagesFile("defaultImages/jigsaw/slidingBlock"));
//        } else {
//            originalCacheMap.putAll(getImagesFile(captchaOriginalPathJigsaw + File.separator + "original"));
//            slidingBlockCacheMap.putAll(getImagesFile(captchaOriginalPathJigsaw + File.separator + "slidingBlock"));
//        }
//        //点选文字
//        if (StringUtils.isBlank(captchaOriginalPathClick)) {
//            picClickCacheMap.putAll(getResourcesImagesFile("defaultImages/pic-click"));
//        } else {
//            picClickCacheMap.putAll(getImagesFile(captchaOriginalPathClick));
//        }
//        fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), originalCacheMap.keySet().toArray(new String[0]));
//        fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), slidingBlockCacheMap.keySet().toArray(new String[0]));
//        fileNameMap.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), picClickCacheMap.keySet().toArray(new String[0]));
//        log.info("初始化底图:{}", JsonUtil.toJSONString(fileNameMap));
    }

    public static void cacheBootImage(Map<String, String> originalMap, Map<String, String> slidingBlockMap,
        Map<String, String> picClickMap) {
        originalCacheMap.putAll(originalMap);
        slidingBlockCacheMap.putAll(slidingBlockMap);
        picClickCacheMap.putAll(picClickMap);
        fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), originalCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), slidingBlockCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), picClickCacheMap.keySet().toArray(new String[0]));
        log.info("自定义resource底图: {}", fileNameMap);
    }

    public static BufferedImage getOriginal(String fileName) {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.ORIGINAL.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = originalCacheMap.get(strings[randomInt]);
        return getBufferedImage(s);
    }

    public static String getslidingBlock() {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = slidingBlockCacheMap.get(strings[randomInt]);
        return s;
    }

    public static BufferedImage getPicClick() throws IOException {
        String s = RandomUtils.pick(picClickCacheMap);
        return getBufferedImage(s);
    }

    /**
     * 图片转base64 字符串
     *
     * @param templateImage
     * @return
     */
    public static String getImageToBase64Str(BufferedImage templateImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(templateImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();

        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(bytes).trim();
    }

    /**
     * base64 字符串转图片
     *
     * @param cachedImage
     * @return
     * @throws IOException
     */
    public static BufferedImage getBufferedImage(CachedImage cachedImage) throws IOException {
        var decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(cachedImage.getBase64());
        try (var inputStream = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(inputStream);
        }
    }

    public static Map<String, CachedImage> getResourcesImagesFile(String path) throws IOException {
        //默认提供六张底图
        Map<String, CachedImage> imgMap = new HashMap<>();
        var classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        resources.asIterator().forEachRemaining((URL url) -> {
            CachedImage cachedImage = new CachedImage();
            try {
                cachedImage.setBase64(Base64Utils.encodeToUrlSafeString(url.openStream().readAllBytes()));
                cachedImage.setFileName(url.getFile());
                imgMap.put(url.getFile(), cachedImage);
            } catch (IOException e) {
                log.error("read default file {} failed!", url.getFile(), e);
            }
        });
        return imgMap;
    }

    public static Map<String, CachedImage> getImagesFile(String path) {
        Map<String, CachedImage> imgMap = new HashMap<>();
        File file = new File(path);
        if (!file.exists()) {
            return new HashMap<>();
        }
        File[] files = file.listFiles();
        Arrays.stream(Objects.requireNonNull(files)).forEach((File item) -> {
            try (FileInputStream fileInputStream = new FileInputStream(item)) {

                CachedImage cachedImage = new CachedImage();
                cachedImage.setBase64(Base64Utils.encodeToUrlSafeString(fileInputStream.readAllBytes()));
                cachedImage.setFileName(item.getName());

                imgMap.put(item.getName(), cachedImage);
            } catch (IOException e) {
                log.error("load image resources from out path failed!", e);
            }
        });
        return imgMap;
    }

}
