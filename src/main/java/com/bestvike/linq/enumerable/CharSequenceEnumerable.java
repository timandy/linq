package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IListEnumerable;
import com.bestvike.linq.enumerator.CharSequenceEnumerator;
import com.bestvike.linq.util.Array;
import com.bestvike.linq.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 许崇雷
 * @date 2017/7/25
 */
public class CharSequenceEnumerable implements IListEnumerable<Character> {
    private final CharSequence source;

    public CharSequenceEnumerable(CharSequence source) {
        this.source = source;
    }

    public CharSequence internalSource() {
        return this.source;
    }

    @Override
    public Character internalGet(int index) {
        return this.source.charAt(index);
    }

    @Override
    public int internalSize() {
        return this.source.length();
    }

    @Override
    public boolean internalContains(Character value) {
        int length = this.source.length();
        for (int i = 0; i < length; i++) {
            if (ObjectUtils.equals(this.source.charAt(i), value))
                return true;
        }
        return false;
    }

    @Override
    public Array<Character> internalToArray() {
        int length = this.source.length();
        Array<Character> array = Array.create(length);
        for (int i = 0; i < length; i++)
            array.set(i, this.source.charAt(i));
        return array;
    }

    @Override
    public Character[] internalToArray(Class<Character> clazz) {
        int length = this.source.length();
        Character[] array = new Character[length];
        for (int i = 0; i < length; i++)
            array[i] = this.source.charAt(i);
        return array;
    }

    @Override
    public List<Character> internalToList() {
        int length = this.source.length();
        List<Character> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++)
            list.add(this.source.charAt(i));
        return list;
    }

    @Override
    public IEnumerator<Character> enumerator() {
        return new CharSequenceEnumerator(this.source);
    }
}
