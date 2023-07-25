package com.github.mbmll.concept;

/**
 * 当我开发一个导入导出数据的功能时, 一些字段需要转换成易于理解的值. 为了提高内聚和复用, 我认为两者互相转换的过程应该放在一起. <br>
 * 极少情况下可以直接 2 转换, 但是这种情况下, {@link com.github.mbmll.concept.SimpleMapper} 显得有些多余.
 *
 * @param <T> 来自外部的数据
 * @param <S> 来自内部的数据
 *
 * @author xlc
 */

public interface SimpleMapper<T, S> extends Mapper<T, S, S, T> {
    @Override
    S parse(T target);

    @Override
    T generate(S source);
}
