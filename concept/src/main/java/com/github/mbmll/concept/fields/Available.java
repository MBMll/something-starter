package com.github.mbmll.concept.fields;

/**
 * 基本可用的实体类(表), 必须包含 id, update time, delete flag.
 *
 * @param <I> id string/long
 * @param <D> update time datetime
 * @param <B> delete flag boolean
 */
public interface Available<I, D, B> extends Id<I>, UpdateTime<D>, DeleteFlag<B> {
}
