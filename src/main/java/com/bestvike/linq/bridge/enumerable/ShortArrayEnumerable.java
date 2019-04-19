package com.bestvike.linq.bridge.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.bridge.enumerator.ShortArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class ShortArrayEnumerable implements IList<Short> {
    private final short[] source;

    public ShortArrayEnumerable(short[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Short> enumerator() {
        return new ShortArrayEnumerator(this.source);
    }

    @Override
    public Short get(int index) {
        return this.source[index];
    }

    @Override
    public Collection<Short> getCollection() {
        return ArrayUtils.toCollection(this._toArray());
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(Short item) {
        for (short value : this.source) {
            if (Objects.equals(value, item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (short item : this.source)
            array[arrayIndex++] = item;
    }

    @Override
    public Short[] _toArray(Class<Short> clazz) {
        int length = this.source.length;
        Short[] array = ArrayUtils.newInstance(clazz, length);
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
    public List<Short> _toList() {
        int length = this.source.length;
        List<Short> list = new ArrayList<>(length);
        for (short item : this.source)
            list.add(item);
        return list;
    }
}
