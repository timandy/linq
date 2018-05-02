package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.impl.grouped.GroupedEnumerable;
import com.bestvike.linq.impl.grouped.GroupedEnumerable2;
import com.bestvike.linq.impl.grouped.GroupedResultEnumerable;
import com.bestvike.linq.impl.grouped.GroupedResultEnumerable2;

/**
 * Created by 许崇雷 on 2018-04-28.
 */
public final class Grouping {
    private Grouping() {
    }

    public static <TSource, TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return new GroupedEnumerable<>(source, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return new GroupedEnumerable<>(source, keySelector, comparer);
    }

    public static <TSource, TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return new GroupedEnumerable2<>(source, keySelector, elementSelector, null);
    }

    public static <TSource, TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedEnumerable2<>(source, keySelector, elementSelector, comparer);
    }

    public static <TSource, TKey, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector) {
        return new GroupedResultEnumerable<>(source, keySelector, resultSelector, null);
    }

    public static <TSource, TKey, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedResultEnumerable<>(source, keySelector, resultSelector, comparer);
    }

    public static <TSource, TKey, TElement, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        return new GroupedResultEnumerable2<>(source, keySelector, elementSelector, resultSelector, null);
    }

    public static <TSource, TKey, TElement, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedResultEnumerable2<>(source, keySelector, elementSelector, resultSelector, comparer);
    }
}
