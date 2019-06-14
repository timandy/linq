package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.ByteArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class ByteArrayEnumerable implements IArray<Byte> {
    private final byte[] source;

    public ByteArrayEnumerable(byte[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Byte> enumerator() {
        return new ByteArrayEnumerator(this.source);
    }

    @Override
    public Object getArray() {
        return this.source;
    }

    @Override
    public Byte get(int index) {
        return this.source[index];
    }

    @Override
    public int _indexOf(Byte item) {
        if (item == null)
            return -1;
        for (int i = 0; i < this.source.length; i++) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _lastIndexOf(Byte item) {
        if (item == null)
            return -1;
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public Collection<Byte> getCollection() {
        return ArrayUtils.toCollection(this._toArray());
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(Byte item) {
        for (byte value : this.source) {
            if (Objects.equals(value, item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (byte item : this.source)
            array[arrayIndex++] = item;
    }

    @Override
    public Byte[] _toArray(Class<Byte> clazz) {
        Byte[] array = ArrayUtils.newInstance(clazz, this.source.length);
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
    public List<Byte> _toList() {
        List<Byte> list = new ArrayList<>(this.source.length);
        for (byte item : this.source)
            list.add(item);
        return list;
    }
}
