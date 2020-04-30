package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArrayList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.IterableEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-07-09.
 */
public final class ArrayListEnumerable<TSource> implements IArrayList<TSource> {
    private final List<TSource> source;

    public ArrayListEnumerable(List<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        return new IterableEnumerator<>(this.source);
    }

    @Override
    public TSource get(int index) {
        return this.source.get(index);
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
    public int _findIndex(Predicate1<TSource> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = 0, length = this.source.size(); i < length; i++) {
            if (match.apply(this.source.get(i)))
                return i;
        }
        return -1;
    }

    @Override
    public int _findLastIndex(Predicate1<TSource> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = this.source.size() - 1; i >= 0; i--) {
            if (match.apply(this.source.get(i)))
                return i;
        }
        return -1;
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
        TSource[] array = ArrayUtils.empty(clazz);
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
