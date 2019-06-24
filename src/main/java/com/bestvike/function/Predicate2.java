package com.bestvike.function;

/**
 * Created by 许崇雷 on 2019-06-24.
 */
@FunctionalInterface
public interface Predicate2<T1, T2> {
    boolean apply(T1 arg1, T2 arg2);
}
