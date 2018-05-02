package com.bestvike.linq.impl.grouped;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.impl.partition.IIListProvider;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-02.
 */
public final class GroupedEnumerable<TSource, TKey> implements IIListProvider<IGrouping<TKey, TSource>> {
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

    public IEnumerator<IGrouping<TKey, TSource>> enumerator() {

        return Lookup.create(this.source, this.keySelector, this.comparer).enumerator();
    }

    public IGrouping<TKey, TSource>[] _toArray(Class<IGrouping<TKey, TSource>> clazz) {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toArray(clazz);
    }

    public Array<IGrouping<TKey, TSource>> _toArray() {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toArray();
    }

    public List<IGrouping<TKey, TSource>> _toList() {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toList();
    }

    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.comparer).getCount();
    }
}
