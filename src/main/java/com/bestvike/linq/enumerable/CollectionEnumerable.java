package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerator.IterableEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2017/7/17.
 */
public final class CollectionEnumerable<TElement> implements ICollection<TElement> {
    private final Collection<TElement> source;

    public CollectionEnumerable(Collection<TElement> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new IterableEnumerator<>(this.source);
    }

    @Override
    public Collection<TElement> getCollection() {
        return this.source;
    }

    @Override
    public int _getCount() {
        return this.source.size();
    }

    @Override
    public boolean _contains(TElement item) {
        return this.source.contains(item);
    }

    @Override
    public void _copyTo(TElement[] array, int arrayIndex) {
        Object[] src = this.source.toArray();
        System.arraycopy(src, 0, array, arrayIndex, src.length);
    }

    @Override
    public void _copyTo(Array<TElement> array, int arrayIndex) {
        Object[] src = this.source.toArray();
        Array.copy(src, 0, array, arrayIndex, src.length);
    }

    @Override
    public TElement[] _toArray(Class<TElement> clazz) {
        int length = this.source.size();
        TElement[] array = ArrayUtils.newInstance(clazz, length);
        return this.source.toArray(array);
    }

    @Override
    public Array<TElement> _toArray() {
        Object[] array = this.source.toArray();
        return Array.create(array);
    }

    @Override
    public List<TElement> _toList() {
        return new ArrayList<>(this.source);
    }
}
