package org.github.mbmll.examples.i18n;

import lombok.extern.slf4j.Slf4j;
import org.github.mbmll.starters.appllication.context.ApplicationContextComponent;
import org.github.mbmll.starters.i18n.I18nUtil;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/25 22:51
 */
@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    @GetMapping("t1")
    public DemoEntity hello(String name) throws Exception {
        LocaleResolver bean = ApplicationContextComponent.getBean(LocaleResolver.class);
        MessageSource messageSource = ApplicationContextComponent.getBean(MessageSource.class);
        I18nUtil i18nUtil = ApplicationContextComponent.getBean(I18nUtil.class);
        log.debug("{} {} {}", bean, i18nUtil, messageSource);
        I18nTestUtil i18nTestUtil = ApplicationContextComponent.getBean(I18nTestUtil.class);
        log.debug("{}", i18nTestUtil);
        return DemoEntity.builder().name(name + ": " + I18nUtil.getMessage("message.number.invalid")).build();
    }

}
