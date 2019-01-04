package com.bestvike.collections.generic;

/**
 * Created by 许崇雷 on 2017-07-12.
 */
public interface IEqualityComparer<T> {
    boolean equals(T x, T y);

    int hashCode(T obj);
}
