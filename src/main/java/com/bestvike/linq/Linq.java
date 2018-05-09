package com.bestvike.linq;

import com.bestvike.linq.iterator.Enumerable;
import com.bestvike.linq.iterator.Range;
import com.bestvike.linq.iterator.Repeat;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 许崇雷 on 2017/7/18.
 */
public final class Linq {
    private Linq() {
    }

    public static <TResult> IEnumerable<TResult> empty() {
        return Enumerable.empty();
    }

    public static <TSource> IEnumerable<TSource> singletonEnumerable(TSource item) {
        return Enumerable.singletonEnumerable(item);
    }

    public static IEnumerable<Boolean> asEnumerable(boolean[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Byte> asEnumerable(byte[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Short> asEnumerable(short[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Integer> asEnumerable(int[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Long> asEnumerable(long[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Float> asEnumerable(float[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Double> asEnumerable(double[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Character> asEnumerable(char[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(TSource[] source) {
        return Enumerable.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(List<TSource> source) {
        return Enumerable.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Collection<TSource> source) {
        return Enumerable.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterable<TSource> source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Character> asEnumerable(CharSequence source) {
        return Enumerable.asEnumerable(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> asEnumerable(Map<TKey, TValue> source) {
        return Enumerable.asEnumerable(source);
    }

    public static IEnumerable<Integer> range(int start, int count) {
        return Range.range(start, count);
    }

    public static <TResult> IEnumerable<TResult> repeat(TResult element, int count) {
        return Repeat.repeat(element, count);
    }
}
