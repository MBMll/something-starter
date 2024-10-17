package com.github.mbmll.example.jpa.service;

import com.github.mbmll.example.jpa.entity.Equipment;
import com.github.mbmll.example.jpa.entity.Equipment_;
import org.springframework.data.jpa.domain.Specification;

public class Sepcifications {
    public static Specification<Equipment> getEquipmentSpecification(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.equal(root.get(Equipment_.STATUS),
                status));
    }
}