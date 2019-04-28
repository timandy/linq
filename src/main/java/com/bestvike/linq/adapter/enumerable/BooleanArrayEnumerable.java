package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.BooleanArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class BooleanArrayEnumerable implements IList<Boolean> {
    private final boolean[] source;

    public BooleanArrayEnumerable(boolean[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Boolean> enumerator() {
        return new BooleanArrayEnumerator(this.source);
    }

    @Override
    public Boolean get(int index) {
        return this.source[index];
    }

    @Override
    public Collection<Boolean> getCollection() {
        return ArrayUtils.toCollection(this._toArray());
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(Boolean item) {
        for (boolean value : this.source) {
            if (Objects.equals(value, item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (boolean item : this.source)
            array[arrayIndex++] = item;
    }

    @Override
    public Boolean[] _toArray(Class<Boolean> clazz) {
        int length = this.source.length;
        Boolean[] array = ArrayUtils.newInstance(clazz, length);
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
    public List<Boolean> _toList() {
        int length = this.source.length;
        List<Boolean> list = new ArrayList<>(length);
        for (boolean item : this.source)
            list.add(item);
        return list;
    }
}
