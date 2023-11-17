package org.github.mbmll.example.es.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author xlc
 * @Description
 * @Date 2023/11/17 12:47
 */

@Data

public class UserInfo {
    private  String address;
    private  String remark;
    private  Date createTime;
    private  float salary;
    private  String name;
    private  int age;
    private  String birthDate;

}
