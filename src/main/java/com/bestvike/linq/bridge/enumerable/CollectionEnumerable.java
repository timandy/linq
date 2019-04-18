package com.bestvike.linq.bridge.enumerable;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.bridge.enumerator.IterableEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2017-07-17.
 */
public final class CollectionEnumerable<TSource> implements ICollection<TSource> {
    private final Collection<TSource> source;

    public CollectionEnumerable(Collection<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        return new IterableEnumerator<>(this.source);
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
        int length = this.source.size();
        TSource[] array = ArrayUtils.newInstance(clazz, length);
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
