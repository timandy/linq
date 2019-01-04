package com.bestvike.linq.bridge.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.bridge.enumerator.IterableEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2017-07-19.
 */
public final class ListEnumerable<TElement> implements IList<TElement> {
    private final List<TElement> source;

    public ListEnumerable(List<TElement> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new IterableEnumerator<>(this.source);
    }

    @Override
    public TElement get(int index) {
        return this.source.get(index);
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
    public void _copyTo(Object[] array, int arrayIndex) {
        Object[] src = this.source.toArray();
        System.arraycopy(src, 0, array, arrayIndex, src.length);
    }

    @Override
    public TElement[] _toArray(Class<TElement> clazz) {
        int length = this.source.size();
        TElement[] array = ArrayUtils.newInstance(clazz, length);
        return this.source.toArray(array);
    }

    @Override
    public Object[] _toArray() {
        return this.source.toArray();
    }

    @Override
    public List<TElement> _toList() {
        return new ArrayList<>(this.source);
    }
}
