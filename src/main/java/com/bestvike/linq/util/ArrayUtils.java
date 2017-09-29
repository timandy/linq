package com.bestvike.linq.util;

import com.bestvike.linq.exception.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2017/7/19.
 */
public final class ArrayUtils {
    private ArrayUtils() {
    }

    public static <T> T[] empty(Class<T> clazz) {
        return newInstance(clazz, 0);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newInstance(Class<T> clazz, int length) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, length);
    }

    public static <T> boolean contains(T[] array, T item) {
        return indexOf(array, item) != -1;
    }

    public static <T> boolean contains(T[] array, T item, int startIndex, int count) {
        return indexOf(array, item, startIndex, count) != -1;
    }

    public static <T> int indexOf(T[] array, T item) {
        return indexOf(array, item, 0, array.length);
    }

    public static <T> int indexOf(T[] array, T item, int startIndex, int count) {
        if (array == null)
            throw Errors.argumentNull("array");
        if (startIndex > array.length)
            throw Errors.argumentOutOfRange("startIndex");
        if (count < 0 || startIndex > array.length - count)
            throw Errors.argumentOutOfRange("count");
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
