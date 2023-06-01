package org.github.mbmll.examples.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/28 21:45
 */
@Component
public class I18nTestUtil implements MessageSourceAware {
    @Override
    public void setMessageSource(MessageSource messageSource) {
        System.out.println(messageSource);
    }
}
