package com.bestvike.linq;

/**
 * Created by 许崇雷 on 2017/7/12.
 */
public interface IEqualityComparer<T> {
    boolean equals(T x, T y);

    int hashCode(T obj);
}
