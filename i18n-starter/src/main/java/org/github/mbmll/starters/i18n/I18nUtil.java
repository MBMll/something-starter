package org.github.mbmll.starters.i18n;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/28 12:40
 */

public class I18nUtil implements MessageSourceAware {
    private static MessageSource messageSource;

    public static String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(code, args, code, locale);
    }

    public static String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(code, null, code, locale);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        String content;
        try {
            content = messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            content = defaultMessage;
        }
        return content;

    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        I18nUtil.messageSource = messageSource;
    }
}
