package com.github.mbmll.concept.fields;

/**
 * register update time
 *
 * @Author xlc
 * @Description
 * @Date 2024/3/9 16:47:15
 */

public interface UpdateTime<T> extends Field {
    void setUpdateTime(T updateTime);

    T getUpdateTime();
}
