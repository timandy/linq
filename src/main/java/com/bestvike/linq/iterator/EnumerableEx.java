package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.enumerable.ArrayEnumerable;
import com.bestvike.linq.enumerable.CharSequenceEnumerable;
import com.bestvike.linq.enumerable.CollectionEnumerable;
import com.bestvike.linq.enumerable.IterableEnumerable;
import com.bestvike.linq.enumerable.ListEnumerable;
import com.bestvike.linq.enumerable.SingletonEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.function.Func2;
import com.bestvike.linq.util.Array;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 许崇雷
 * @date 2017/7/21
 */
@SuppressWarnings("Duplicates")
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

    public static IEnumerable<Character> asEnumerable(CharSequence source) {
        if (source == null) throw Errors.argumentNull("source");
        return new CharSequenceEnumerable(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> asEnumerable(Map<TKey, TValue> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new CollectionEnumerable<>(source.entrySet());
    }

    public static <TSource> IEnumerable<TSource> singletonEnumerable(TSource item) {
        return new SingletonEnumerable<>(item);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector, comparer);
    }

    public static <TOuter, TInner, TResult> IEnumerable<TResult> crossJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new CrossJoinIterator<>(outer, inner, resultSelector);
    }

    public static <TSource> IEnumerable<TSource> append(IEnumerable<TSource> source, TSource item) {
        if (source == null) throw Errors.argumentNull("source");
        return new AppendIterator<>(source, item);
    }
}
