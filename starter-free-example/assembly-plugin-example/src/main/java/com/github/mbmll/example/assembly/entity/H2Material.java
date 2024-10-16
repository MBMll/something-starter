package com.github.mbmll.example.assembly.entity;

import com.github.mbmll.concept.fields.Available;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author xlc
 * @Description
 * @Date 2024/10/13 00:52:28
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "h2_material")
public class H2Material implements Available<Long, Date, Boolean> {
    @Id
    private Long id;
    private String name;
    private Date updateTime;
    private Boolean deleteFlag;
}
