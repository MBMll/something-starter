package com.github.mbmll.concept.fields;

/**
 * @param <T>
 */
public interface UpdateId<T>
    extends Field {
    /**
     * @param updateId
     */
    void setUpdateId(T updateId);

    T getUpdateId();
}
