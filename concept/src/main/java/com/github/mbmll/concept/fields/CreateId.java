package com.github.mbmll.concept.fields;

/**
 * @param <T>
 */
public interface CreateId<T>
    extends Field {
    /**
     * @return
     */
    T getCreateId();

    /**
     * @param createId
     */
    void setCreateId(T createId);
}
