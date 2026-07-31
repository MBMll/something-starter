package com.github.mbmll.concept.fields;

public interface CreateTime<T>
    extends Field {
    /**
     * @param createTime
     */
    void setCreateTime(T createTime);

    T getCreateTime();
}
