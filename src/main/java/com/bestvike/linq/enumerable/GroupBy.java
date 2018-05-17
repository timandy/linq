package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.exception.Errors;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public final class GroupBy {
    private GroupBy() {
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


final class GroupedEnumerable<TSource, TKey> implements IIListProvider<IGrouping<TKey, TSource>> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;

    public GroupedEnumerable(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        this.source = source;
        this.keySelector = keySelector;
        this.comparer = comparer;
    }

    @Override
    public IEnumerator<IGrouping<TKey, TSource>> enumerator() {

        return Lookup.create(this.source, this.keySelector, this.comparer).enumerator();
    }

    @Override
    public IGrouping<TKey, TSource>[] _toArray(Class<IGrouping<TKey, TSource>> clazz) {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toArray();
    }

    @Override
    public List<IGrouping<TKey, TSource>> _toList() {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toList();
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.comparer).getCount();
    }
}


final class GroupedEnumerable2<TSource, TKey, TElement> implements IIListProvider<IGrouping<TKey, TElement>> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final Func1<TSource, TElement> elementSelector;
    private final IEqualityComparer<TKey> comparer;

    public GroupedEnumerable2(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (elementSelector == null)
            throw Errors.argumentNull("elementSelector");

        this.source = source;
        this.keySelector = keySelector;
        this.elementSelector = elementSelector;
        this.comparer = comparer;
    }

    @Override
    public IEnumerator<IGrouping<TKey, TElement>> enumerator() {
        return Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer).enumerator();
    }

    @Override
    public IGrouping<TKey, TElement>[] _toArray(Class<IGrouping<TKey, TElement>> clazz) {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup._toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup._toArray();
    }

    @Override
    public List<IGrouping<TKey, TElement>> _toList() {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup._toList();
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer).getCount();
    }
}


final class GroupedResultEnumerable<TSource, TKey, TResult> implements IIListProvider<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;
    private final Func2<TKey, IEnumerable<TSource>, TResult> resultSelector;

    public GroupedResultEnumerable(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        this.source = source;
        this.keySelector = keySelector;
        this.resultSelector = resultSelector;
        this.comparer = comparer;
    }

    @Override
    public IEnumerator<TResult> enumerator() {
        Lookup<TKey, TSource> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup.applyResultSelector(this.resultSelector).enumerator();
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toArray(clazz, this.resultSelector);
    }

    @Override
    public Object[] _toArray() {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toArray(this.resultSelector);
    }

    @Override
    public List<TResult> _toList() {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toList(this.resultSelector);
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.comparer).getCount();
    }
}


final class GroupedResultEnumerable2<TSource, TKey, TElement, TResult> implements IIListProvider<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final Func1<TSource, TElement> elementSelector;
    private final IEqualityComparer<TKey> comparer;
    private final Func2<TKey, IEnumerable<TElement>, TResult> resultSelector;

    public GroupedResultEnumerable2(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (elementSelector == null)
            throw Errors.argumentNull("elementSelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        this.source = source;
        this.keySelector = keySelector;
        this.elementSelector = elementSelector;
        this.comparer = comparer;
        this.resultSelector = resultSelector;
    }

    @Override
    public IEnumerator<TResult> enumerator() {
        Lookup<TKey, TElement> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup.applyResultSelector(this.resultSelector).enumerator();
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        return Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer)._toArray(clazz, this.resultSelector);
    }

    @Override
    public Object[] _toArray() {
        return Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer)._toArray(this.resultSelector);
    }

    @Override
    public List<TResult> _toList() {
        return Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer)._toList(this.resultSelector);
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer).getCount();
    }
}
