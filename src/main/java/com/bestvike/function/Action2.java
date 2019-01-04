package com.bestvike.function;

/**
 * Created by 许崇雷 on 2017-07-10.
 */
@FunctionalInterface
public interface Action2<T1, T2> {
    void apply(T1 arg1, T2 arg2);
}
