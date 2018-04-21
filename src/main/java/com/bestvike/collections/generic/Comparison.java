package com.bestvike.collections.generic;

/**
 * Created by 许崇雷 on 2018-04-21.
 */
@FunctionalInterface
public interface Comparison<T> {
    int compare(T x, T y);
}
