package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.enumerable.CharSequenceEnumerable;
import com.bestvike.linq.enumerable.CollectionEnumerable;
import com.bestvike.linq.enumerable.IterableEnumerable;
import com.bestvike.linq.enumerable.ListEnumerable;
import com.bestvike.linq.exception.Errors;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class Enumerable {
    private Enumerable() {
    }

    public static <TResult> IEnumerable<TResult> empty() {
        return Array.empty();
    }

    public static <TSource> IEnumerable<TSource> singletonEnumerable(TSource item) {
        return Array.singleton(item);
    }

    public static IEnumerable<Boolean> asEnumerable(boolean[] source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return Array.create(source);
    }

    public static IEnumerable<Byte> asEnumerable(byte[] source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return Array.create(source);
    }

    public static IEnumerable<Short> asEnumerable(short[] source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return Array.create(source);
    }

    public static IEnumerable<Integer> asEnumerable(int[] source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return Array.create(source);
    }

    public static IEnumerable<Long> asEnumerable(long[] source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return Array.create(source);
    }

    public static IEnumerable<Float> asEnumerable(float[] source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return Array.create(source);
    }

    public static IEnumerable<Double> asEnumerable(double[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return Array.create(source);
    }

    public static IEnumerable<Character> asEnumerable(char[] source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return Array.create(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(TSource[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return Array.create(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(List<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return new ListEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Collection<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return new CollectionEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return new IterableEnumerable<>(source);
    }

    public static IEnumerable<Character> asEnumerable(CharSequence source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return new CharSequenceEnumerable(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> asEnumerable(Map<TKey, TValue> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return new CollectionEnumerable<>(source.entrySet());
    }
}
