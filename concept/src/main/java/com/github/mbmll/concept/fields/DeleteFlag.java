package com.github.mbmll.concept.fields;

public interface DeleteFlag<T> extends Field {
    T getDeleteFlag();

    void setDeleteFlag(T deleteFlag);
}
