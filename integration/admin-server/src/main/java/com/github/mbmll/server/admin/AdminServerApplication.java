package com.github.mbmll.server.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @Author xlc
 * @Description
 * @Date 2024/3/23 18:11
 */
@SpringBootApplication
@EnableWebSecurity
public class AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }
}
