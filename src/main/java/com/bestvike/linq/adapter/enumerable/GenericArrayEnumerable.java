package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.GenericArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-04-12.
 */
public final class GenericArrayEnumerable<TSource> implements IArray<TSource> {
    private final TSource[] source;

    public GenericArrayEnumerable(TSource[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        return new GenericArrayEnumerator<>(this.source);
    }

    @Override
    public TSource[] getArray() {
        return this.source;
    }

    @Override
    public TSource get(int index) {
        return this.source[index];
    }

    @Override
    public int _indexOf(TSource item) {
        return ArrayUtils.indexOf(this.source, item);
    }

    @Override
    public int _lastIndexOf(TSource item) {
        return ArrayUtils.lastIndexOf(this.source, item);
    }

    @Override
    public int _findIndex(Predicate1<TSource> match) {
        return ArrayUtils.findIndex(this.source, match);
    }

    @Override
    public int _findLastIndex(Predicate1<TSource> match) {
        return ArrayUtils.findLastIndex(this.source, match);
    }

    @Override
    public Collection<TSource> getCollection() {
        return ArrayUtils.toCollection(this.source);
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(TSource item) {
        return ArrayUtils.contains(this.source, item);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        System.arraycopy(this.source, 0, array, arrayIndex, this.source.length);
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        return ArrayUtils.toArray(this.source, clazz);
    }

    @Override
    public Object[] _toArray() {
        return ArrayUtils.toArray(this.source, Object.class);
    }

    @Override
    public List<TSource> _toList() {
        return ArrayUtils.toList(this.source);
    }
}
