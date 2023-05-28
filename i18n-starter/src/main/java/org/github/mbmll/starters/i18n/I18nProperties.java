package org.github.mbmll.starters.i18n;

import java.nio.charset.Charset;
import lombok.Data;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/28 14:50
 */
@Data
public class I18nProperties {
    // from message source
    private String defaultEncoding = Charset.defaultCharset().toString();
    private Boolean useCodeAsDefaultMessage = false;
    private Integer cacheMillis = -1;
}
