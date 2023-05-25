package org.github.mbmll.examples.response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/25 22:51
 */
@RestController
@RequestMapping("response-packing")
public class DemoController {

    @GetMapping("hello")
    public DemoEntity hello(String name) {
        return DemoEntity.builder().name(name).build();
    }
}
