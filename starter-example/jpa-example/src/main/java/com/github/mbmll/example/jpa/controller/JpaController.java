package com.github.mbmll.example.jpa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mbmll.example.jpa.service.H2Service;
import com.github.mbmll.example.jpa.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jpa")
public class JpaController {
    @Autowired
    private H2Service h2Service;
    @Autowired
    private JpaService jpaService;

    private static String getString(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }

    //    @GetMapping("/name/specification")
//    public String nameAndSpecification() throws JsonProcessingException {
//        return getString(jpaService.nameAndSpecification());
//    }
    @GetMapping("/specification")
    public String name() throws JsonProcessingException {
        return getString(jpaService.specification());
    }

    @GetMapping("/order")
    public String order() throws JsonProcessingException {
        return getString(jpaService.orderEquipment());
    }
}
