package com.bestvike.linq.util;

public final class ObjectUtils {
    private ObjectUtils() {
    }

    public static boolean equals(Object x, Object y) {
        return x == null ? y == null : x.equals(y);
    }
}
