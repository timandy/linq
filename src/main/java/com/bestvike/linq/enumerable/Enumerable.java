package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.adapter.enumerable.BooleanArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.ByteArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.CharSequenceEnumerable;
import com.bestvike.linq.adapter.enumerable.CharacterArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.CollectionEnumerable;
import com.bestvike.linq.adapter.enumerable.DoubleArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.EnumerationEnumerable;
import com.bestvike.linq.adapter.enumerable.FloatArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.GenericArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.IntegerArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.IterableEnumerable;
import com.bestvike.linq.adapter.enumerable.IteratorEnumerable;
import com.bestvike.linq.adapter.enumerable.ListEnumerable;
import com.bestvike.linq.adapter.enumerable.LongArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.ShortArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.SingletonEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
@SuppressWarnings("unchecked")
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

    public static <TSource> IEnumerable<TSource> asEnumerable(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return source;
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

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterator<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new IteratorEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Enumeration<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new EnumerationEnumerable<>(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> asEnumerable(Map<TKey, TValue> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CollectionEnumerable<>(source.entrySet());
    }

    public static <TSource> IEnumerable<TSource> ofEnumerable(Object source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof boolean[])
            return (IEnumerable<TSource>) asEnumerable((boolean[]) source);
        if (source instanceof byte[])
            return (IEnumerable<TSource>) asEnumerable((byte[]) source);
        if (source instanceof short[])
            return (IEnumerable<TSource>) asEnumerable((short[]) source);
        if (source instanceof int[])
            return (IEnumerable<TSource>) asEnumerable((int[]) source);
        if (source instanceof long[])
            return (IEnumerable<TSource>) asEnumerable((long[]) source);
        if (source instanceof float[])
            return (IEnumerable<TSource>) asEnumerable((float[]) source);
        if (source instanceof double[])
            return (IEnumerable<TSource>) asEnumerable((double[]) source);
        if (source instanceof char[])
            return (IEnumerable<TSource>) asEnumerable((char[]) source);
        if (source instanceof CharSequence)
            return (IEnumerable<TSource>) asEnumerable((CharSequence) source);
        if (source instanceof Object[])
            return asEnumerable((TSource[]) source);
        if (source instanceof IEnumerable)
            return asEnumerable((IEnumerable<TSource>) source);
        if (source instanceof List)
            return asEnumerable((List<TSource>) source);
        if (source instanceof Collection)
            return asEnumerable((Collection<TSource>) source);
        if (source instanceof Iterable)
            return asEnumerable((Iterable<TSource>) source);
        if (source instanceof Iterator)
            return asEnumerable((Iterator<TSource>) source);
        if (source instanceof Enumeration)
            return asEnumerable((Enumeration<TSource>) source);
        if (source instanceof Map)
            return (IEnumerable<TSource>) asEnumerable((Map<?, ?>) source);
        ThrowHelper.throwNotSupportedException();
        return null;
    }
}
