package org.github.mbmll.exmaples.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
public class DemoController {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("");
        return javaMailSender;
    }

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText("simple mail message");
        return simpleMailMessage;
    }

    @GetMapping("t1")
    public DemoEntity hello(String name) throws Exception {
        return DemoEntity.builder().name(name).build();
    }

}
