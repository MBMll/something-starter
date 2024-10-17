package com.github.mbmll.concept.fields;

public interface Essential<I, D, B> extends Id<I>, CreateTime<D>, UpdateTime<D>, Deleted<B> {
}
