package com.bestvike.function;

/**
 * Created by 许崇雷 on 2019-06-28.
 */
@FunctionalInterface
public interface IndexFunc2<T, TResult> {
    TResult apply(T arg1, int index);
}
