package com.bestvike.linq.util;

import com.bestvike.linq.IEqualityComparer;

/**
 * @author 许崇雷
 * @date 2017/7/18
 */
public final class EqualityComparer {
    private EqualityComparer() {
    }

    public static <T> IEqualityComparer<T> Default() {
        return new IEqualityComparer<T>() {
            @Override
            public boolean equals(T x, T y) {
                return ObjectUtils.equals(x, y);
            }

            @Override
            public int getHashCode(T obj) {
                return obj == null ? 0 : obj.hashCode();
            }
        };
    }
}
