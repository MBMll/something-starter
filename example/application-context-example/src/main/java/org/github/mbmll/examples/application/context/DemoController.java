package org.github.mbmll.examples.application.context;

import org.github.mbmll.starters.appllication.context.ApplicationContextCompoment;
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
    public DemoEntity hello(String name) {
        LocaleResolver bean = ApplicationContextCompoment.getBean(LocaleResolver.class);
        return DemoEntity.builder().name(name).build();
    }

}
