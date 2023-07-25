package com.github.mbmll.concept;

/**
 * 基础的 2 类型转换 {@link SimpleMapper} 很多时候不适用. 因为在 java 中, 往往存在复杂的类型转换,这里需要对类型进行预处理.
 *
 * @param <T>
 * @param <S>
 *
 * @see SimpleMapper
 */

public interface Mapper<T, S, E, U> {

    S parse(U u);

    T generate(E e);

}
