package com.github.mbmll.concept.fields;

public interface Deleted<T> extends Field {
    T getDeleted();

    void setDeleted(T deleteFlag);
}
