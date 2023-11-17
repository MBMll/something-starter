package org.github.mbmll.examples.response;

import org.github.mbmll.starters.logaspect.LogPointcut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/25 22:51
 */
@LogPointcut
@RestController
@RequestMapping("demo")
public class DemoController {

    @GetMapping("t1")
    public DemoEntity hello(String name) {
        return DemoEntity.builder().name(name).build();
    }
}
