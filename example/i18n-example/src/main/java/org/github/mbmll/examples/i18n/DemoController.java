package org.github.mbmll.examples.i18n;

import org.github.mbmll.starters.appllication.context.ApplicationContextUtil;
import org.github.mbmll.starters.i18n.I18nUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/25 22:51
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @GetMapping("t1")
    public DemoEntity hello(String name) throws Exception {
//        throw new Exception();
        LocaleResolver bean = ApplicationContextUtil.getBean(LocaleResolver.class);
        return DemoEntity.builder().name(name + ": " + I18nUtil.getMessage("message.number.invalid")).build();
    }

}
