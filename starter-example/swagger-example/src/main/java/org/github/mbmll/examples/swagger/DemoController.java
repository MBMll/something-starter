package org.github.mbmll.examples.swagger;

import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/25 22:51
 */
@Slf4j
@RestController
@RequestMapping("demo")
@ApiModel(value = "demo", description = "demo desc")
public class DemoController {

    @GetMapping("list-buckets")
    public DemoEntity hello(String name) throws Exception {
//        return minioClient.listBuckets();
        return DemoEntity.builder().name(name).build();
    }

}
