package com.github.mbmll.server.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class IndexController {


    @GetMapping("index")
    public String index() {
        return "index" + UUID.randomUUID().toString();
    }
}
