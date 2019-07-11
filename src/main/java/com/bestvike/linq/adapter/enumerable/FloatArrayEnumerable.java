package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.FloatArrayEnumerator;
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
public final class FloatArrayEnumerable implements IArray<Float> {
    private final float[] source;

    public FloatArrayEnumerable(float[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Float> enumerator() {
        return new FloatArrayEnumerator(this.source);
    }

    @Override
    public Object getArray() {
        return this.source;
    }

    @Override
    public Float get(int index) {
        return this.source[index];
    }

    @Override
    public int _indexOf(Float item) {
        if (item == null)
            return -1;
        for (int i = 0; i < this.source.length; i++) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _lastIndexOf(Float item) {
        if (item == null)
            return -1;
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _findIndex(Predicate1<Float> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = 0; i < this.source.length; i++) {
            if (match.apply(this.source[i]))
                return i;
        }
        return -1;
    }

    @Override
    public int _findLastIndex(Predicate1<Float> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (match.apply(this.source[i]))
                return i;
        }
        return -1;
    }

    @Override
    public Collection<Float> getCollection() {
        return ArrayUtils.toCollection(this._toArray());
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(Float item) {
        for (float value : this.source) {
            if (Objects.equals(value, item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (float item : this.source)
            array[arrayIndex++] = item;
    }

    @Override
    public Float[] _toArray(Class<Float> clazz) {
        Float[] array = ArrayUtils.newInstance(clazz, this.source.length);
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
    public List<Float> _toList() {
        List<Float> list = new ArrayList<>(this.source.length);
        for (float item : this.source)
            list.add(item);
        return list;
    }
}
