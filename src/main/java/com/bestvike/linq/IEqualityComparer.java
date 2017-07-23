package com.bestvike.linq;

/**
 * @author 许崇雷
 * @date 2017/7/12
 */
public interface IEqualityComparer<T> {
    boolean equals(T x, T y);

    int hashCode(T obj);
}
