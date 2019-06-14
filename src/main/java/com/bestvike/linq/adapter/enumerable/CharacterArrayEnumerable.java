package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.CharacterArrayEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class CharacterArrayEnumerable implements IArray<Character> {
    private final char[] source;

    public CharacterArrayEnumerable(char[] source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Character> enumerator() {
        return new CharacterArrayEnumerator(this.source);
    }

    @Override
    public Object getArray() {
        return this.source;
    }

    @Override
    public Character get(int index) {
        return this.source[index];
    }

    @Override
    public int _indexOf(Character item) {
        if (item == null)
            return -1;
        for (int i = 0; i < this.source.length; i++) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _lastIndexOf(Character item) {
        if (item == null)
            return -1;
        for (int i = this.source.length - 1; i >= 0; i--) {
            if (this.source[i] == item)
                return i;
        }
        return -1;
    }

    @Override
    public Collection<Character> getCollection() {
        return ArrayUtils.toCollection(this._toArray());
    }

    @Override
    public int _getCount() {
        return this.source.length;
    }

    @Override
    public boolean _contains(Character item) {
        for (char value : this.source) {
            if (Objects.equals(value, item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (char item : this.source)
            array[arrayIndex++] = item;
    }

    @Override
    public Character[] _toArray(Class<Character> clazz) {
        Character[] array = ArrayUtils.newInstance(clazz, this.source.length);
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
    public List<Character> _toList() {
        List<Character> list = new ArrayList<>(this.source.length);
        for (char item : this.source)
            list.add(item);
        return list;
    }
}
