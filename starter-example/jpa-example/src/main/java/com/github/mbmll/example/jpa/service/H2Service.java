package com.github.mbmll.example.jpa.service;

import com.github.mbmll.example.jpa.repository.EquipmentRepository;
import com.github.mbmll.example.jpa.repository.H2MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author xlc
 * @Description
 * @Date 2024/10/13 00:50:21
 */
@Service
public class H2Service {
    @Autowired
    private H2MaterialRepository h2MaterialRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;

}
