package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IListEnumerable;
import com.bestvike.linq.enumerator.SingletonEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.Array;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 许崇雷
 * @date 2017/7/21
 */
public final class SingletonEnumerable<TElement> implements IListEnumerable<TElement> {
    private final TElement element;

    public SingletonEnumerable(TElement element) {
        this.element = element;
    }

    @Override
    public TElement internalGet(int index) {
        if (index == 0) return this.element;
        throw Errors.argumentOutOfRange("index");
    }

    @Override
    public int internalSize() {
        return 1;
    }

    @Override
    public boolean internalContains(TElement value) {
        return Objects.equals(this.element, value);
    }

    @Override
    public Array<TElement> internalToArray() {
        Array<TElement> array = Array.create(1);
        array.set(0, this.element);
        return array;
    }

    @Override
    public TElement[] internalToArray(Class<TElement> clazz) {
        TElement[] array = ArrayUtils.newInstance(clazz, 1);
        array[0] = this.element;
        return array;
    }

    @Override
    public List<TElement> internalToList() {
        List<TElement> list = new ArrayList<>(1);
        list.add(this.element);
        return list;
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new SingletonEnumerator<>(this.element);
    }
}
