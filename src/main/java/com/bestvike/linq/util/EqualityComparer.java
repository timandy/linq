package com.bestvike.linq.util;

import com.bestvike.linq.IEqualityComparer;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2017/7/18.
 */
public final class EqualityComparer {
    private EqualityComparer() {
    }

    public static <T> IEqualityComparer<T> Default() {
        return new IEqualityComparer<T>() {
            @Override
            public boolean equals(T x, T y) {
                return Objects.equals(x, y);
            }

            @Override
            public int hashCode(T obj) {
                return obj == null ? 0 : obj.hashCode();
            }
        };
    }
}
