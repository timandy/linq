package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IListEnumerable;
import com.bestvike.linq.enumerator.IterableEnumerator;
import com.bestvike.linq.util.CollectionUtils;

import java.util.List;

/**
 * Created by 许崇雷 on 2017/7/19.
 */
public final class ListEnumerable<TElement> implements IListEnumerable<TElement> {
    private final List<TElement> source;

    public ListEnumerable(List<TElement> source) {
        this.source = source;
    }

    public List<TElement> internalSource() {
        return this.source;
    }

    @Override
    public TElement internalGet(int index) {
        return this.source.get(index);
    }

    @Override
    public int internalSize() {
        return this.source.size();
    }

    @Override
    public boolean internalContains(TElement value) {
        return this.source.contains(value);
    }

    @Override
    public Array<TElement> internalToArray() {
        return CollectionUtils.toArray(this.source);
    }

    @Override
    public TElement[] internalToArray(Class<TElement> clazz) {
        return CollectionUtils.toArray(this.source, clazz);
    }

    @Override
    public List<TElement> internalToList() {
        return CollectionUtils.toList(this.source);
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new IterableEnumerator<>(this.source);
    }
}
