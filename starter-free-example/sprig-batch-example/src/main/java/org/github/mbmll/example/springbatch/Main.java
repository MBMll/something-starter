package org.github.mbmll.example.springbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author xlc
 * @Description
 * @Date 2024/1/7 13:51:00
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(Main.class, args)));
    }

}
