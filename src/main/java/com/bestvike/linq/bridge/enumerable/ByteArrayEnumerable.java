package com.bestvike.linq.bridge.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.bridge.enumerator.ByteArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class ByteArrayEnumerable implements IList<Byte> {
    private final byte[] source;

    public ByteArrayEnumerable(byte[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Byte> enumerator() {
        return new ByteArrayEnumerator(this.source);
    }

    @Override
    public Byte get(int index) {
        return this.source[index];
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
        int length = this.source.length;
        Byte[] array = ArrayUtils.newInstance(clazz, length);
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
    public List<Byte> _toList() {
        int length = this.source.length;
        List<Byte> list = new ArrayList<>(length);
        for (byte item : this.source)
            list.add(item);
        return list;
    }
}
