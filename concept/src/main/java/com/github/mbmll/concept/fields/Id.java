package com.github.mbmll.concept.fields;

public interface Id<T> extends Field {
    T getId();

    void setId(T id);
}
