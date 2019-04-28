package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.DoubleArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class DoubleArrayEnumerable implements IArray<Double> {
    private final double[] source;

    public DoubleArrayEnumerable(double[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Double> enumerator() {
        return new DoubleArrayEnumerator(this.source);
    }

    @Override
    public Object getArray() {
        return this.source;
    }

    @Override
    public Double get(int index) {
        return this.source[index];
    }

    @Override
    public Collection<Double> getCollection() {
        return ArrayUtils.toCollection(this._toArray());
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(Double item) {
        for (double value : this.source) {
            if (Objects.equals(value, item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (double item : this.source)
            array[arrayIndex++] = item;
    }

    @Override
    public Double[] _toArray(Class<Double> clazz) {
        int length = this.source.length;
        Double[] array = ArrayUtils.newInstance(clazz, length);
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
    public List<Double> _toList() {
        int length = this.source.length;
        List<Double> list = new ArrayList<>(length);
        for (double item : this.source)
            list.add(item);
        return list;
    }
}
