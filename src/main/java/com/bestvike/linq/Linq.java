package com.bestvike.linq;

import com.bestvike.linq.enumerable.Enumerable;
import com.bestvike.linq.enumerable.Range;
import com.bestvike.linq.enumerable.Repeat;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 许崇雷 on 2017-07-18.
 */
@SuppressWarnings("unchecked")
public final class Linq {
    private Linq() {
    }

    public static <TResult> IEnumerable<TResult> empty() {
        return Enumerable.empty();
    }

    public static <TSource> IEnumerable<TSource> singleton(TSource item) {
        return Enumerable.singleton(item);
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

    public static IEnumerable<Character> asEnumerable(CharSequence source) {
        return Enumerable.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(TSource... source) {
        return Enumerable.asEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(IEnumerable<? extends TSource> source) {
        return Enumerable.asEnumerable((IEnumerable<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(List<? extends TSource> source) {
        return Enumerable.asEnumerable((List<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Collection<? extends TSource> source) {
        return Enumerable.asEnumerable((Collection<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterable<? extends TSource> source) {
        return Enumerable.asEnumerable((Iterable<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterator<? extends TSource> source) {
        return Enumerable.asEnumerable((Iterator<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Enumeration<? extends TSource> source) {
        return Enumerable.asEnumerable((Enumeration<TSource>) source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> asEnumerable(Map<? extends TKey, ? extends TValue> source) {
        return Enumerable.asEnumerable((Map<TKey, TValue>) source);
    }

    public static <TSource> IEnumerable<TSource> ofEnumerable(Object source) {
        return Enumerable.ofEnumerable(source);
    }

    public static IEnumerable<Integer> range(int start, int count) {
        return Range.range(start, count);
    }

    public static <TResult> IEnumerable<TResult> repeat(TResult element, int count) {
        return Repeat.repeat(element, count);
    }
}
