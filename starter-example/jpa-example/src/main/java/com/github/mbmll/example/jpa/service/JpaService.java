package com.github.mbmll.example.jpa.service;

import com.github.mbmll.example.jpa.entity.Equipment;
import com.github.mbmll.example.jpa.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xlc
 * @Description
 * @Date 2024/10/17 21:05:47
 */
@Service
public class JpaService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    private static Sort getUpdateTime() {
        return Sort.by(Sort.Order.desc("updateTime"));
    }

//    public List<Equipment> nameAndSpecification() {
//        return equipmentRepository.findBy( getEquipmentSpecification(), FluentQuery.FetchableFluentQuery::all
//        equipmentRepository.findAll(getEquipmentSpecification(), Pageable.unpaged());
//    }

    public List<Equipment> orderEquipment() {
        return equipmentRepository.findAll(Sepcifications.getEquipmentSpecification("正常"), getUpdateTime());
    }

    public List<Equipment> specification() {
        return equipmentRepository.findAll();
    }
}
