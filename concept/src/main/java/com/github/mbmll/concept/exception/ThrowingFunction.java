package com.github.mbmll.concept.exception;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
    R apply(T t)
        throws E;
}
