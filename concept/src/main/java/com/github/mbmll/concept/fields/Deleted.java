package com.github.mbmll.concept.fields;

public interface Deleted<T>
    extends Field {
    /**
     * @return
     */
    T getDeleted();

    void setDeleted(T deleteFlag);
}
