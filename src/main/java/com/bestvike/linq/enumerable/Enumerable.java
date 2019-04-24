package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.bridge.enumerable.BooleanArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.ByteArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.CharSequenceEnumerable;
import com.bestvike.linq.bridge.enumerable.CharacterArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.CollectionEnumerable;
import com.bestvike.linq.bridge.enumerable.DoubleArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.FloatArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.GenericArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.IntegerArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.IterableEnumerable;
import com.bestvike.linq.bridge.enumerable.ListEnumerable;
import com.bestvike.linq.bridge.enumerable.LongArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.ShortArrayEnumerable;
import com.bestvike.linq.bridge.enumerable.SingletonEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

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
        return EmptyPartition.instance();
    }

    public static <TSource> IEnumerable<TSource> singleton(TSource item) {
        return new SingletonEnumerable<>(item);
    }

    public static IEnumerable<Boolean> asEnumerable(boolean[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new BooleanArrayEnumerable(source);
    }

    public static IEnumerable<Byte> asEnumerable(byte[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new ByteArrayEnumerable(source);
    }

    public static IEnumerable<Short> asEnumerable(short[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new ShortArrayEnumerable(source);
    }

    public static IEnumerable<Integer> asEnumerable(int[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new IntegerArrayEnumerable(source);
    }

    public static IEnumerable<Long> asEnumerable(long[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new LongArrayEnumerable(source);
    }

    public static IEnumerable<Float> asEnumerable(float[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new FloatArrayEnumerable(source);
    }

    public static IEnumerable<Double> asEnumerable(double[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new DoubleArrayEnumerable(source);
    }

    public static IEnumerable<Character> asEnumerable(char[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CharacterArrayEnumerable(source);
    }

    public static IEnumerable<Character> asEnumerable(CharSequence source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CharSequenceEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(TSource[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new GenericArrayEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(List<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new ListEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Collection<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CollectionEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new IterableEnumerable<>(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> asEnumerable(Map<TKey, TValue> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CollectionEnumerable<>(source.entrySet());
    }
}
