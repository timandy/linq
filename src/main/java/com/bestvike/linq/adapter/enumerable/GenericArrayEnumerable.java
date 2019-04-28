package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.GenericArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-04-12.
 */
public final class GenericArrayEnumerable<TElement> implements IArray<TElement> {
    private final TElement[] source;

    public GenericArrayEnumerable(TElement[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new GenericArrayEnumerator<>(this.source);
    }

    @Override
    public Object getArray() {
        return this.source;
    }

    @Override
    public TElement get(int index) {
        return this.source[index];
    }

    @Override
    public Collection<TElement> getCollection() {
        return ArrayUtils.toCollection(this.source);
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(TElement item) {
        return ArrayUtils.contains(this.source, item);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        System.arraycopy(this.source, 0, array, arrayIndex, this.source.length);
    }

    @Override
    public TElement[] _toArray(Class<TElement> clazz) {
        return ArrayUtils.toArray(this.source, clazz);
    }

    @Override
    public Object[] _toArray() {
        return ArrayUtils.toArray(this.source, Object.class);
    }

    @Override
    public List<TElement> _toList() {
        return ArrayUtils.toList(this.source);
    }
}
