package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IListEnumerable;
import com.bestvike.linq.enumerator.ArrayEnumerator;
import com.bestvike.linq.util.Array;

import java.util.List;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
public final class ArrayEnumerable<TElement> implements IListEnumerable<TElement> {
    private final Array<TElement> source;

    public ArrayEnumerable(Array<TElement> source) {
        this.source = source;
    }

    public Array<TElement> internalSource() {
        return this.source;
    }

    @Override
    public TElement internalGet(int index) {
        return this.source.get(index);
    }

    @Override
    public int internalSize() {
        return this.source.length();
    }

    @Override
    public boolean internalContains(TElement value) {
        return this.source.contains(value);
    }

    @Override
    public Array<TElement> internalToArray() {
        return this.source.clone();
    }

    @Override
    public TElement[] internalToArray(Class<TElement> clazz) {
        return this.source.toArray(clazz);
    }

    @Override
    public List<TElement> internalToList() {
        return this.source.toList();
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new ArrayEnumerator<>(this.source);
    }
}
