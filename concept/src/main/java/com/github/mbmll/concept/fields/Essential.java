package com.github.mbmll.concept.fields;

/**
 * 类似 mybatis 的通用字段
 *
 * @param <I>
 * @param <D>
 * @param <B>
 */
public interface Essential<I, D, B>
    extends Id<I>, CreateTime<D>, UpdateTime<D>, Deleted<B> {
}
