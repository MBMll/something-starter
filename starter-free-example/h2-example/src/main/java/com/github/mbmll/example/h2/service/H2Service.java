package com.github.mbmll.example.h2.service;

import com.github.mbmll.example.h2.entity.H2Material;
import com.github.mbmll.example.h2.repository.H2MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xlc
 * @Description
 * @Date 2024/10/13 00:50:21
 */
@Service
public class H2Service {
    @Autowired
    private H2MaterialRepository h2MaterialRepository;

    public List<H2Material> getAll() {
        return h2MaterialRepository.findAll();
    }
}
