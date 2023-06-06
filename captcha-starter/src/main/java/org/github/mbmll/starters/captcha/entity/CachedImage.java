package org.github.mbmll.starters.captcha.entity;

import java.awt.image.BufferedImage;
import lombok.Data;

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
