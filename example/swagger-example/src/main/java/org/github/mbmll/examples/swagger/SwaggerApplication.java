package org.github.mbmll.examples.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/28 16:08
 */
@SpringBootApplication
@EnableOpenApi
public class SwaggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerApplication.class, args);
    }
}
