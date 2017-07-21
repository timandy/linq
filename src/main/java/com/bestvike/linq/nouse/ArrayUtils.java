//package com.bestvike.linq.util;
//
//import com.bestvike.linq.exception.ArgumentNullException;
//import com.bestvike.linq.exception.ArgumentOutOfRangeException;
//
//import java.util.Objects;
//
///**
// * @author 许崇雷
// * @date 2017/7/11
// */
//public final class ArrayUtils {
//    private ArrayUtils() {
//    }
//
//    public static <T> int indexOf(T[] array, T item) {
//        return indexOf(array, item, 0, array.length);
//    }
//
//    public static <T> int indexOf(T[] array, T item, int startIndex, int count) {
//        for (int i = startIndex, len = startIndex + count; i < len; i++) {
//            if (Objects.equals(item, array[i]))
//                return i;
//        }
//        return -1;
//    }
//
//    public static <T> int lastIndexOf(T[] array, T item) {
//        return lastIndexOf(array, item, 0, array.length);
//    }
//
//    public static <T> int lastIndexOf(T[] array, T item, int startIndex, int count) {
//        for (int i = startIndex + count - 1; i >= startIndex; i--) {
//            if (Objects.equals(item, array[i]))
//                return i;
//        }
//        return -1;
//    }
//}
