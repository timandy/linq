package com.bestvike;

/**
 * Created by 许崇雷 on 2018-04-21.
 */
@FunctionalInterface
public interface IComparison<T> {
    int compare(T x, T y);
}
