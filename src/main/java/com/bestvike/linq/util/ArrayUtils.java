package com.bestvike.linq.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 许崇雷
 * @date 2017/7/19
 */
public final class ArrayUtils {
    private ArrayUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newInstance(Class<T> clazz, int length) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, length);
    }

    public static <T> boolean contains(T[] array, T item) {
        return indexOf(array, item) != -1;
    }

    public static <T> int indexOf(T[] array, T item) {
        return indexOf(array, item, 0, array.length);
    }

    public static <T> int indexOf(T[] array, T item, int startIndex, int count) {
        for (int i = startIndex, len = startIndex + count; i < len; i++) {
            if (Objects.equals(item, array[i]))
                return i;
        }
        return -1;
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    public static <T> T[] toArray(Object[] source, Class<T> clazz) {
        int length = source.length;
        T[] array = newInstance(clazz, length);
        if (length > 0)
            System.arraycopy(source, 0, array, 0, length);
        return array;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object[] source) {
        int length = source.length;
        List<T> list = new ArrayList<>(length);
        for (Object item : source)
            list.add((T) item);
        return list;
    }
}
