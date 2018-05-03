package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerator.CharSequenceEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2017/7/25.
 */
public final class CharSequenceEnumerable implements IList<Character> {
    private final CharSequence source;

    public CharSequenceEnumerable(CharSequence source) {
        this.source = source;
    }

    @Override
    public IEnumerator<Character> enumerator() {
        return new CharSequenceEnumerator(this.source);
    }

    @Override
    public Collection<Character> getCollection() {
        return ArrayUtils.toCollection(this._toArray(Character.class));
    }

    @Override
    public int _getCount() {
        return this.source.length();
    }

    @Override
    public boolean _contains(Character value) {
        int length = this.source.length();
        for (int i = 0; i < length; i++) {
            if (Objects.equals(this.source.charAt(i), value))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Character[] array, int arrayIndex) {
        int length = this.source.length();
        for (int i = 0; i < length; i++)
            array[arrayIndex++] = this.source.charAt(i);
    }

    @Override
    public void _copyTo(Array<Character> array, int arrayIndex) {
        int length = this.source.length();
        for (int i = 0; i < length; i++)
            array.set(arrayIndex++, this.source.charAt(i));
    }

    @Override
    public Character[] _toArray(Class<Character> clazz) {
        int length = this.source.length();
        Character[] array = new Character[length];
        for (int i = 0; i < length; i++)
            array[i] = this.source.charAt(i);
        return array;
    }

    @Override
    public Array<Character> _toArray() {
        int length = this.source.length();
        Array<Character> array = Array.create(length);
        for (int i = 0; i < length; i++)
            array.set(i, this.source.charAt(i));
        return array;
    }

    @Override
    public List<Character> _toList() {
        int length = this.source.length();
        List<Character> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++)
            list.add(this.source.charAt(i));
        return list;
    }

    @Override
    public Character get(int index) {
        return this.source.charAt(index);
    }
}
