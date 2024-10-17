package com.github.mbmll.example.jpa.entity;

import com.github.mbmll.concept.fields.MaterialEssential;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@Getter
@Setter
@ToString
@FieldNameConstants
@RequiredArgsConstructor
@Entity
@Table(name = "h2_equipment")
public class Equipment implements MaterialEssential<Long, Date, Boolean> {
    @Id
    private Long id;
    private String name;
    private Date createTime;
    private Date updateTime;
    private Boolean deleted;

    private String status;
    private String description;
}
