package com.bestvike.linq;

import com.bestvike.linq.enumerable.ArrayEnumerable;
import com.bestvike.linq.enumerable.CollectionEnumerable;
import com.bestvike.linq.enumerable.IterableEnumerable;
import com.bestvike.linq.enumerable.ListEnumerable;
import com.bestvike.linq.enumerable.SingletonEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.iterator.AppendIterator;
import com.bestvike.linq.util.Array;

import java.util.Collection;
import java.util.List;

/**
 * @author 许崇雷
 * @date 2017/7/21
 */
public final class EnumerableEx {
    private EnumerableEx() {
    }

    public static IEnumerable<Boolean> asEnumerable(boolean[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Byte> asEnumerable(byte[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Short> asEnumerable(short[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Integer> asEnumerable(int[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Long> asEnumerable(long[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Float> asEnumerable(float[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Double> asEnumerable(double[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Character> asEnumerable(char[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(TSource[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(List<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ListEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Collection<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new CollectionEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new IterableEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> singletonEnumerable(TSource item) {
        return new SingletonEnumerable<>(item);
    }

    public static <TSource> IEnumerable<TSource> append(IEnumerable<TSource> source, TSource item) {
        if (source == null) throw Errors.argumentNull("source");
        return new AppendIterator<>(source, item);
    }
}
