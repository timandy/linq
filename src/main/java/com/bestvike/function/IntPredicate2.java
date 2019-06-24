package com.bestvike.function;

/**
 * Created by 许崇雷 on 2019-06-24.
 */
@FunctionalInterface
public interface IntPredicate2<T> {
    boolean apply(T arg1, int arg2);
}
