package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.adapter.enumerable.ArrayListEnumerable;
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
import com.bestvike.linq.adapter.enumerable.LinkedListEnumerable;
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
import java.util.RandomAccess;

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

    public static IEnumerable<Boolean> of(boolean[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new BooleanArrayEnumerable(source);
    }

    public static IEnumerable<Byte> of(byte[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new ByteArrayEnumerable(source);
    }

    public static IEnumerable<Short> of(short[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new ShortArrayEnumerable(source);
    }

    public static IEnumerable<Integer> of(int[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new IntegerArrayEnumerable(source);
    }

    public static IEnumerable<Long> of(long[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new LongArrayEnumerable(source);
    }

    public static IEnumerable<Float> of(float[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new FloatArrayEnumerable(source);
    }

    public static IEnumerable<Double> of(double[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new DoubleArrayEnumerable(source);
    }

    public static IEnumerable<Character> of(char[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CharacterArrayEnumerable(source);
    }

    public static IEnumerable<Character> of(CharSequence source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CharSequenceEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> of(TSource[] source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new GenericArrayEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return source;
    }

    public static <TSource> IEnumerable<TSource> of(List<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return source instanceof RandomAccess ? new ArrayListEnumerable<>(source) : new LinkedListEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Collection<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CollectionEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Iterable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new IterableEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Iterator<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new IteratorEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Enumeration<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new EnumerationEnumerable<>(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> of(Map<TKey, TValue> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new CollectionEnumerable<>(source.entrySet());
    }

    public static <TSource> IEnumerable<TSource> as(Object source) {
        if (source == null)
            return null;
        if (source instanceof boolean[])
            return (IEnumerable<TSource>) of((boolean[]) source);
        if (source instanceof byte[])
            return (IEnumerable<TSource>) of((byte[]) source);
        if (source instanceof short[])
            return (IEnumerable<TSource>) of((short[]) source);
        if (source instanceof int[])
            return (IEnumerable<TSource>) of((int[]) source);
        if (source instanceof long[])
            return (IEnumerable<TSource>) of((long[]) source);
        if (source instanceof float[])
            return (IEnumerable<TSource>) of((float[]) source);
        if (source instanceof double[])
            return (IEnumerable<TSource>) of((double[]) source);
        if (source instanceof char[])
            return (IEnumerable<TSource>) of((char[]) source);
        if (source instanceof CharSequence)
            return (IEnumerable<TSource>) of((CharSequence) source);
        if (source instanceof Object[])
            return of((TSource[]) source);
        if (source instanceof IEnumerable)
            return of((IEnumerable<TSource>) source);
        if (source instanceof List)
            return of((List<TSource>) source);
        if (source instanceof Collection)
            return of((Collection<TSource>) source);
        if (source instanceof Iterable)
            return of((Iterable<TSource>) source);
        if (source instanceof Iterator)
            return of((Iterator<TSource>) source);
        if (source instanceof Enumeration)
            return of((Enumeration<TSource>) source);
        if (source instanceof Map)
            return (IEnumerable<TSource>) of((Map<?, ?>) source);
        return null;
    }
}
