package org.github.mbmll.example.quartz;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author xlc
 * @Description
 * @Date 2024/6/18 23:58:39
 */
@SpringBootApplication
@EnableScheduling
public class QuartzApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
