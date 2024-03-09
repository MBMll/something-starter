package com.github.mbmll.concept.fields;

public interface CreateId<T> extends Field {
    void setCreateId(T createId);

    T getCreateId();
}
