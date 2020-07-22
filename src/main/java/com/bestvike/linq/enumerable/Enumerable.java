package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerable.ArrayListEnumerable;
import com.bestvike.linq.adapter.enumerable.BooleanArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.ByteArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.CharEnumerable;
import com.bestvike.linq.adapter.enumerable.CharacterArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.CollectionEnumerable;
import com.bestvike.linq.adapter.enumerable.DoubleArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.EnumerationEnumerable;
import com.bestvike.linq.adapter.enumerable.EnumeratorEnumerable;
import com.bestvike.linq.adapter.enumerable.FloatArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.GenericArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.IntegerArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.IterableEnumerable;
import com.bestvike.linq.adapter.enumerable.IteratorEnumerable;
import com.bestvike.linq.adapter.enumerable.LineEnumerable;
import com.bestvike.linq.adapter.enumerable.LinkedListEnumerable;
import com.bestvike.linq.adapter.enumerable.LongArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.ShortArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.SingletonEnumerable;
import com.bestvike.linq.adapter.enumerable.SpliteratorEnumerable;
import com.bestvike.linq.adapter.enumerable.StreamEnumerable;
import com.bestvike.linq.adapter.enumerable.WordEnumerable;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.stream.Stream;

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

    public static <TSource> IEnumerable<TSource> ofNullable(TSource item) {
        return item == null ? EmptyPartition.instance() : new SingletonEnumerable<>(item);
    }

    public static IEnumerable<Boolean> of(boolean[] source) {
        return source == null ? EmptyPartition.instance() : new BooleanArrayEnumerable(source);
    }

    public static IEnumerable<Byte> of(byte[] source) {
        return source == null ? EmptyPartition.instance() : new ByteArrayEnumerable(source);
    }

    public static IEnumerable<Short> of(short[] source) {
        return source == null ? EmptyPartition.instance() : new ShortArrayEnumerable(source);
    }

    public static IEnumerable<Integer> of(int[] source) {
        return source == null ? EmptyPartition.instance() : new IntegerArrayEnumerable(source);
    }

    public static IEnumerable<Long> of(long[] source) {
        return source == null ? EmptyPartition.instance() : new LongArrayEnumerable(source);
    }

    public static IEnumerable<Float> of(float[] source) {
        return source == null ? EmptyPartition.instance() : new FloatArrayEnumerable(source);
    }

    public static IEnumerable<Double> of(double[] source) {
        return source == null ? EmptyPartition.instance() : new DoubleArrayEnumerable(source);
    }

    public static IEnumerable<Character> of(char[] source) {
        return source == null ? EmptyPartition.instance() : new CharacterArrayEnumerable(source);
    }

    public static <TSource> IEnumerable<TSource> of(TSource[] source) {
        return source == null ? EmptyPartition.instance() : new GenericArrayEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(List<TSource> source) {
        return source == null ? EmptyPartition.instance() : source instanceof RandomAccess ? new ArrayListEnumerable<>(source) : new LinkedListEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Collection<TSource> source) {
        return source == null ? EmptyPartition.instance() : new CollectionEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(IEnumerable<TSource> source) {
        return source == null ? EmptyPartition.instance() : source;
    }

    public static <TSource> IEnumerable<TSource> of(IEnumerator<TSource> source) {
        return source == null ? EmptyPartition.instance() : new EnumeratorEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Iterable<TSource> source) {
        return source == null ? EmptyPartition.instance() : new IterableEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Iterator<TSource> source) {
        return source == null ? EmptyPartition.instance() : new IteratorEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Stream<TSource> source) {
        return source == null ? EmptyPartition.instance() : new StreamEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Spliterator<TSource> source) {
        return source == null ? EmptyPartition.instance() : new SpliteratorEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> of(Enumeration<TSource> source) {
        return source == null ? EmptyPartition.instance() : new EnumerationEnumerable<>(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> of(Map<TKey, TValue> source) {
        return source == null ? EmptyPartition.instance() : new CollectionEnumerable<>(source.entrySet());
    }

    public static <TSource> IEnumerable<TSource> as(Object source) {
        if (source == null)
            return EmptyPartition.instance();
        if (source instanceof boolean[])
            return (IEnumerable<TSource>) new BooleanArrayEnumerable((boolean[]) source);
        if (source instanceof byte[])
            return (IEnumerable<TSource>) new ByteArrayEnumerable((byte[]) source);
        if (source instanceof short[])
            return (IEnumerable<TSource>) new ShortArrayEnumerable((short[]) source);
        if (source instanceof int[])
            return (IEnumerable<TSource>) new IntegerArrayEnumerable((int[]) source);
        if (source instanceof long[])
            return (IEnumerable<TSource>) new LongArrayEnumerable((long[]) source);
        if (source instanceof float[])
            return (IEnumerable<TSource>) new FloatArrayEnumerable((float[]) source);
        if (source instanceof double[])
            return (IEnumerable<TSource>) new DoubleArrayEnumerable((double[]) source);
        if (source instanceof char[])
            return (IEnumerable<TSource>) new CharacterArrayEnumerable((char[]) source);
        if (source instanceof Object[])
            return new GenericArrayEnumerable<>((TSource[]) source);
        if (source instanceof List)
            return source instanceof RandomAccess ? new ArrayListEnumerable<>((List<TSource>) source) : new LinkedListEnumerable<>((List<TSource>) source);
        if (source instanceof Collection)
            return new CollectionEnumerable<>((Collection<TSource>) source);
        if (source instanceof IEnumerable)
            return (IEnumerable<TSource>) source;
        if (source instanceof Iterable)
            return new IterableEnumerable<>((Iterable<TSource>) source);
        if (source instanceof Stream)
            return new StreamEnumerable<>((Stream<TSource>) source);
        if (source instanceof IEnumerator)
            return new EnumeratorEnumerable<>((IEnumerator<TSource>) source);
        if (source instanceof Iterator)
            return new IteratorEnumerable<>((Iterator<TSource>) source);
        if (source instanceof Spliterator)
            return new SpliteratorEnumerable<>((Spliterator<TSource>) source);
        if (source instanceof Enumeration)
            return new EnumerationEnumerable<>((Enumeration<TSource>) source);
        if (source instanceof Map)
            return (IEnumerable<TSource>) new CollectionEnumerable<>(((Map<?, ?>) source).entrySet());
        return null;
    }

    public static IEnumerable<Character> chars(CharSequence source) {
        return source == null ? EmptyPartition.instance() : new CharEnumerable(source);
    }

    public static IEnumerable<String> words(CharSequence source) {
        return source == null ? EmptyPartition.instance() : new WordEnumerable(source);
    }

    public static IEnumerable<String> lines(CharSequence source) {
        return source == null ? EmptyPartition.instance() : new LineEnumerable(source);
    }
}
