package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.CharSequenceEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2017-07-25.
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
    public Character get(int index) {
        return this.source.charAt(index);
    }

    @Override
    public int _indexOf(Character item) {
        if (item == null)
            return -1;
        for (int i = 0, length = this.source.length(); i < length; i++) {
            if (this.source.charAt(i) == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _lastIndexOf(Character item) {
        if (item == null)
            return -1;
        for (int i = this.source.length() - 1; i >= 0; i--) {
            if (this.source.charAt(i) == item)
                return i;
        }
        return -1;
    }

    @Override
    public int _findIndex(Predicate1<Character> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = 0, length = this.source.length(); i < length; i++) {
            if (match.apply(this.source.charAt(i)))
                return i;
        }
        return -1;
    }

    @Override
    public int _findLastIndex(Predicate1<Character> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = this.source.length() - 1; i >= 0; i--) {
            if (match.apply(this.source.charAt(i)))
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
        return this.source.length();
    }

    @Override
    public boolean _contains(Character item) {
        for (int i = 0, length = this.source.length(); i < length; i++) {
            if (Objects.equals(this.source.charAt(i), item))
                return true;
        }
        return false;
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        for (int i = 0, length = this.source.length(); i < length; i++)
            array[arrayIndex++] = this.source.charAt(i);
    }

    @Override
    public Character[] _toArray(Class<Character> clazz) {
        Character[] array = ArrayUtils.newInstance(clazz, this.source.length());
        for (int i = 0; i < array.length; i++)
            array[i] = this.source.charAt(i);
        return array;
    }

    @Override
    public Object[] _toArray() {
        Object[] array = new Object[this.source.length()];
        for (int i = 0; i < array.length; i++)
            array[i] = this.source.charAt(i);
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
}
