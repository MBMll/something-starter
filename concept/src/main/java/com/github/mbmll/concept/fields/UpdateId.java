package com.github.mbmll.concept.fields;

public interface UpdateId<T> extends Field {
    void setUpdateId(T updateId);

    T getUpdateId();
}
