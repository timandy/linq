package com.bestvike.linq.enumerable;

import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IOrderedEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2018-05-03.
 */
public final class OrderBy {
    private OrderBy() {
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> orderBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return new OrderedEnumerable<>(source, keySelector, null, false, null);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> orderBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        return new OrderedEnumerable<>(source, keySelector, comparer, false, null);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> orderByDescending(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return new OrderedEnumerable<>(source, keySelector, null, true, null);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> orderByDescending(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        return new OrderedEnumerable<>(source, keySelector, comparer, true, null);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> thenBy(IOrderedEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return source.createOrderedEnumerable(keySelector, null, false);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> thenBy(IOrderedEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return source.createOrderedEnumerable(keySelector, comparer, false);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> thenByDescending(IOrderedEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return source.createOrderedEnumerable(keySelector, null, true);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> thenByDescending(IOrderedEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return source.createOrderedEnumerable(keySelector, comparer, true);
    }
}
