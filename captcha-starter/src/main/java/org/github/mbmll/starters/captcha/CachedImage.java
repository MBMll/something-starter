package org.github.mbmll.starters.captcha;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/5 9:11
 */
@Data
public class CachedImage {
    private String fileName;
    private String base64;
    private BufferedImage bufferedImage;
}
