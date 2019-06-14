package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.IntegerArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class IntegerArrayEnumerable implements IArray<Integer> {
    private final int[] source;

    public IntegerArrayEnumerable(int[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Integer> enumerator() {
        return new IntegerArrayEnumerator(this.source);
    }

    @Override
    public Object getArray() {
        return this.source;
    }

    @Override
    public Integer get(int index) {
        return this.source[index];
    }

    @Override
    public int _indexOf(Integer item) {
        if (item == null)
            return -1;
        for (int i = 0; i < this.source.length; i++) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _lastIndexOf(Integer item) {
        if (item == null)
            return -1;
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public Collection<Integer> getCollection() {
        return ArrayUtils.toCollection(this._toArray());
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(Integer item) {
        for (int value : this.source) {
            if (Objects.equals(value, item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (int item : this.source)
            array[arrayIndex++] = item;
    }

    @Override
    public Integer[] _toArray(Class<Integer> clazz) {
        Integer[] array = ArrayUtils.newInstance(clazz, this.source.length);
        for (int i = 0; i < array.length; i++)
            array[i] = this.source[i];
        return array;
    }

    @Override
    public Object[] _toArray() {
        Object[] array = new Object[this.source.length];
        for (int i = 0; i < array.length; i++)
            array[i] = this.source[i];
        return array;
    }

    @Override
    public List<Integer> _toList() {
        List<Integer> list = new ArrayList<>(this.source.length);
        for (int item : this.source)
            list.add(item);
        return list;
    }
}
