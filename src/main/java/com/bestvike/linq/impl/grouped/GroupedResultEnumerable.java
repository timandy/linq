package com.bestvike.linq.impl.grouped;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.impl.partition.IIListProvider;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-02.
 */
public final class GroupedResultEnumerable<TSource, TKey, TResult> implements IIListProvider<TResult> {
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

    public IEnumerator<TResult> enumerator() {
        Lookup<TKey, TSource> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup.applyResultSelector(this.resultSelector).enumerator();
    }

    public TResult[] _toArray(Class<TResult> clazz) {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toArray(clazz, this.resultSelector);
    }

    public Array<TResult> _toArray() {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toArray(this.resultSelector);
    }

    public List<TResult> _toList() {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toList(this.resultSelector);
    }

    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.comparer).getCount();
    }
}
