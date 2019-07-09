package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.ILinkedList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.IterableEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-07-09.
 */
public class LinkedListEnumerable<TSource> implements ILinkedList<TSource> {
    protected final List<TSource> source;

    public LinkedListEnumerable(List<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        return new IterableEnumerator<>(this.source);
    }

    @Override
    public int _indexOf(TSource item) {
        return this.source.indexOf(item);
    }

    @Override
    public int _lastIndexOf(TSource item) {
        return this.source.lastIndexOf(item);
    }

    @Override
    public Collection<TSource> getCollection() {
        return this.source;
    }

    @Override
    public int _getCount() {
        return this.source.size();
    }

    @Override
    public boolean _contains(TSource item) {
        return this.source.contains(item);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        Object[] src = this.source.toArray();
        System.arraycopy(src, 0, array, arrayIndex, src.length);
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        TSource[] array = ArrayUtils.newInstance(clazz, this.source.size());
        return this.source.toArray(array);
    }

    @Override
    public Object[] _toArray() {
        return this.source.toArray();
    }

    @Override
    public List<TSource> _toList() {
        return new ArrayList<>(this.source);
    }
}
