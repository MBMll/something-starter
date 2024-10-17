package com.github.mbmll.example.jpa.controller;


import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * @Author xlc
 * @Description
 * @Date 2024/10/17 23:42:14
 */

public class JpaControllerTest {

    @Test
    public void testOrder() {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(restTemplate.getForObject("http://localhost:8080/example/jpa/order", String.class));
    }
}