package com.bestvike.linq.util;

import com.bestvike.linq.exception.Errors;

import java.util.Comparator;

/**
 * @author 许崇雷
 * @date 2017/7/18
 */
public final class Comparer {
    private Comparer() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> Default() {
        return (x, y) -> {
            if (x == y)
                return 0;
            if (x == null)
                return -1;
            if (y == null)
                return 1;
            if (x instanceof Comparable)
                return ((Comparable) x).compareTo(y);
            if (y instanceof Comparable)
                return -((Comparable) y).compareTo(x);
            throw Errors.implementComparable();
        };
    }
}
