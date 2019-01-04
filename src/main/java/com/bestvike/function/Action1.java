package com.bestvike.function;

/**
 * Created by 许崇雷 on 2017-07-10.
 */
@FunctionalInterface
public interface Action1<T> {
    void apply(T arg);
}
