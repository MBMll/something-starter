package com.github.mbmll.concept.fields;

import lombok.Data;

import java.util.Date;

/**
 *
 */
@Data
public class MergeDemo implements Time<Date>, Update<String, Date>, Create<String, Date> {
    private String id;
    private String name;
    private String createId;
    private Date createTime;
    private String updateId;
    private Date updateTime;
}
