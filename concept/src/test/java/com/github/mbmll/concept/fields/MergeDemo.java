package com.github.mbmll.concept.fields;

import lombok.Data;
import org.junit.Test;

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

    public <T extends Time<Date>> void convert(T target) {

    }

    @Test
    public void test() {
        MergeDemo mergeDemo = new MergeDemo();
//        入参 和 形参 都要来自同一个接口,不能认为组合成的接口和被组合的接口完全相等
        mergeDemo.convert(mergeDemo);
    }
}
