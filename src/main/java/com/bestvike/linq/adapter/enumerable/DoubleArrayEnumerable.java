package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.DoubleArrayEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
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
    public int _indexOf(Double item) {
        if (item == null)
            return -1;
        for (int i = 0; i < this.source.length; i++) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _lastIndexOf(Double item) {
        if (item == null)
            return -1;
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _findIndex(Predicate1<Double> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = 0; i < this.source.length; i++) {
            if (match.apply(this.source[i]))
                return i;
        }
        return -1;
    }

    @Override
    public int _findLastIndex(Predicate1<Double> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (match.apply(this.source[i]))
                return i;
        }
        return -1;
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
        Double[] array = ArrayUtils.newInstance(clazz, this.source.length);
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
    public List<Double> _toList() {
        List<Double> list = new ArrayList<>(this.source.length);
        for (double item : this.source)
            list.add(item);
        return list;
    }
}
