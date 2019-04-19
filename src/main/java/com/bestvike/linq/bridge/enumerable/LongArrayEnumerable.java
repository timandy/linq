package com.bestvike.linq.bridge.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.bridge.enumerator.LongArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class LongArrayEnumerable implements IList<Long> {
    private final long[] source;

    public LongArrayEnumerable(long[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Long> enumerator() {
        return new LongArrayEnumerator(this.source);
    }

    @Override
    public Long get(int index) {
        return this.source[index];
    }

    @Override
    public Collection<Long> getCollection() {
        return ArrayUtils.toCollection(this._toArray());
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(Long item) {
        for (long value : this.source) {
            if (Objects.equals(value, item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (long item : this.source)
            array[arrayIndex++] = item;
    }

    @Override
    public Long[] _toArray(Class<Long> clazz) {
        int length = this.source.length;
        Long[] array = ArrayUtils.newInstance(clazz, length);
        for (int i = 0; i < length; i++)
            array[i] = this.source[i];
        return array;
    }

    @Override
    public Object[] _toArray() {
        int length = this.source.length;
        Object[] array = new Object[length];
        for (int i = 0; i < length; i++)
            array[i] = this.source[i];
        return array;
    }

    @Override
    public List<Long> _toList() {
        int length = this.source.length;
        List<Long> list = new ArrayList<>(length);
        for (long item : this.source)
            list.add(item);
        return list;
    }
}
