package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.ShortArrayEnumerator;
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
public final class ShortArrayEnumerable implements IArray<Short> {
    private final short[] source;

    public ShortArrayEnumerable(short[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Short> enumerator() {
        return new ShortArrayEnumerator(this.source);
    }

    @Override
    public Object getArray() {
        return this.source;
    }

    @Override
    public Short get(int index) {
        return this.source[index];
    }

    @Override
    public int _indexOf(Short item) {
        if (item == null)
            return -1;
        for (int i = 0; i < this.source.length; i++) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _lastIndexOf(Short item) {
        if (item == null)
            return -1;
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _findIndex(Predicate1<Short> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = 0; i < this.source.length; i++) {
            if (match.apply(this.source[i]))
                return i;
        }
        return -1;
    }

    @Override
    public int _findLastIndex(Predicate1<Short> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (match.apply(this.source[i]))
                return i;
        }
        return -1;
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
        Short[] array = ArrayUtils.newInstance(clazz, this.source.length);
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
    public List<Short> _toList() {
        List<Short> list = new ArrayList<>(this.source.length);
        for (short item : this.source)
            list.add(item);
        return list;
    }
}
