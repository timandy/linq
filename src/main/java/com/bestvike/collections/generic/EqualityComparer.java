package com.bestvike.collections.generic;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2017/7/18.
 */
public final class EqualityComparer<T> implements IEqualityComparer<T> {
    private static final EqualityComparer DEFAULT = new EqualityComparer();

    private EqualityComparer() {
    }

    @SuppressWarnings("unchecked")
    public static <T> EqualityComparer<T> Default() {
        return DEFAULT;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(T obj) {
        return obj == null ? 0 : obj.hashCode();
    }

    public int indexOf(Object[] array, Object value, int startIndex, int count) {
        int num = startIndex + count;
        for (int index = startIndex; index < num; ++index) {
            if (this.equals(array[index], value))
                return index;
        }
        return -1;
    }

    public int lastIndexOf(T[] array, T value, int startIndex, int count) {
        int num = startIndex - count + 1;
        for (int index = startIndex; index >= num; --index) {
            if (this.equals(array[index], value))
                return index;
        }
        return -1;
    }
}
