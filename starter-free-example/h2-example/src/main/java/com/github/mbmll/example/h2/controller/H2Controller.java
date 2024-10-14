package com.github.mbmll.example.h2.controller;

import com.github.mbmll.example.h2.service.H2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/h2")
public class H2Controller {
    @Autowired
    private H2Service h2Service;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping(value = "/index")
    public String index() {
        return h2Service.getAll().toString();
    }
}
