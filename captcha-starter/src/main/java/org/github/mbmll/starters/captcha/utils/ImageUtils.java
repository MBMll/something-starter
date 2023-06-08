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

import static org.github.mbmll.starters.captcha.Const.IMAGE_TYPE_PNG;

@Slf4j
public class ImageUtils {

    public static Map<String, CachedImage> cacheImage(String outPath, String defaultPath) throws IOException {
        if (StringUtils.isBlank(outPath)) {
            return getResourcesImagesFile(defaultPath);
        }
        return getImagesFile(outPath);
    }

    /**
     * 图片转base64 字符串
     *
     * @param templateImage
     * @return
     */
    public static String getBase64(BufferedImage templateImage) throws IOException {
        byte[] bytes;
        try (var baos = new ByteArrayOutputStream()) {
            ImageIO.write(templateImage, IMAGE_TYPE_PNG, baos);
            bytes = baos.toByteArray();
        }
        var encoder = Base64.getEncoder();
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
