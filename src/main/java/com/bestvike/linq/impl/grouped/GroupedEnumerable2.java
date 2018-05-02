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
public final class GroupedEnumerable2<TSource, TKey, TElement> implements IIListProvider<IGrouping<TKey, TElement>> {
    private final IEnumerable<TSource> _source;
    private final Func1<TSource, TKey> _keySelector;
    private final Func1<TSource, TElement> _elementSelector;
    private final IEqualityComparer<TKey> _comparer;

    public GroupedEnumerable2(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (elementSelector == null)
            throw Errors.argumentNull("elementSelector");

        this._source = source;
        this._keySelector = keySelector;
        this._elementSelector = elementSelector;
        this._comparer = comparer;
    }

    public IEnumerator<IGrouping<TKey, TElement>> enumerator() {
        return Lookup.create(this._source, this._keySelector, this._elementSelector, this._comparer).enumerator();
    }

    public IGrouping<TKey, TElement>[] _toArray(Class<IGrouping<TKey, TElement>> clazz) {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this._source, this._keySelector, this._elementSelector, this._comparer);
        return lookup._toArray(clazz);
    }

    public Array<IGrouping<TKey, TElement>> _toArray() {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this._source, this._keySelector, this._elementSelector, this._comparer);
        return lookup._toArray();
    }

    public List<IGrouping<TKey, TElement>> _toList() {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this._source, this._keySelector, this._elementSelector, this._comparer);
        return lookup._toList();
    }

    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this._source, this._keySelector, this._elementSelector, this._comparer).getCount();
    }
}
