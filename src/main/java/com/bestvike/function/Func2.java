package com.bestvike.function;

/**
 * Created by 许崇雷 on 2017/7/10.
 */
@FunctionalInterface
public interface Func2<T1, T2, TResult> {
    TResult apply(T1 arg1, T2 arg2);
}
