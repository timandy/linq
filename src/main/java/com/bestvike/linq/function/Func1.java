package com.bestvike.linq.function;

/**
 * Created by 许崇雷 on 2017/7/10.
 */
@FunctionalInterface
public interface Func1<T, TResult> {
    TResult apply(T arg);
}
