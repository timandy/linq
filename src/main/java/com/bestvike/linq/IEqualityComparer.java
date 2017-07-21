package com.bestvike.linq;

import java.util.Objects;

/**
 * @author 许崇雷
 * @date 2017/7/12
 */
public interface IEqualityComparer<T> {


    boolean equals(T x, T y);

    int getHashCode(T obj);
}
