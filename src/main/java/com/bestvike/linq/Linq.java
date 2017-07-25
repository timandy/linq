package com.bestvike.linq;

import com.bestvike.linq.iterator.Enumerable;
import com.bestvike.linq.iterator.EnumerableEx;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 许崇雷
 * @date 2017/7/18
 */
public final class Linq {
    private Linq() {
    }

    public static IEnumerable<Boolean> asEnumerable(boolean[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static IEnumerable<Byte> asEnumerable(byte[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static IEnumerable<Short> asEnumerable(short[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static IEnumerable<Integer> asEnumerable(int[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static IEnumerable<Long> asEnumerable(long[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static IEnumerable<Float> asEnumerable(float[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static IEnumerable<Double> asEnumerable(double[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static IEnumerable<Character> asEnumerable(char[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(TSource[] source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(List<TSource> source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Collection<TSource> source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterable<TSource> source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static IEnumerable<Character> asEnumerable(CharSequence source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> asEnumerable(Map<TKey, TValue> source) {
        return EnumerableEx.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> singletonEnumerable(TSource item) {
        return EnumerableEx.singletonEnumerable(item);
    }

    public static IEnumerable<Integer> range(int start, int count) {
        return Enumerable.range(start, count);
    }

    public static <TResult> IEnumerable<TResult> repeat(TResult element, int count) {
        return Enumerable.repeat(element, count);
    }

    public static <TResult> IEnumerable<TResult> empty() {
        return Enumerable.empty();
    }
}
