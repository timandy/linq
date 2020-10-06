package com.bestvike.collections.generic;

import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.ArrayEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2017-07-19.
 */
public final class Array<T> implements IArray<T>, Cloneable {
    private static final Array<?> EMPTY = new Array<>(ArrayUtils.empty());
    private final Object[] elements;

    public Array(Object[] elements) {
        if (elements == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.elements);
        this.elements = elements;
    }

    public static <T> Array<T> empty() {
        //noinspection unchecked
        return (Array<T>) EMPTY;
    }

    @Override
    public IEnumerator<T> enumerator() {
        return new ArrayEnumerator<>(this.elements);
    }

    @Override
    public Object[] getArray() {
        return this.elements;
    }

    @Override
    public T get(int index) {
        //noinspection unchecked
        return (T) this.elements[index];
    }

    @Override
    public int _indexOf(T item) {
        return ArrayUtils.indexOf(this.elements, item);
    }

    @Override
    public int _lastIndexOf(T item) {
        return ArrayUtils.lastIndexOf(this.elements, item);
    }

    @Override
    public int _findIndex(Predicate1<T> match) {
        //noinspection unchecked
        return ArrayUtils.findIndex(this.elements, (Predicate1<Object>) match);
    }

    @Override
    public int _findLastIndex(Predicate1<T> match) {
        //noinspection unchecked
        return ArrayUtils.findLastIndex(this.elements, (Predicate1<Object>) match);
    }

    public void set(int index, T item) {
        this.elements[index] = item;
    }

    @Override
    public Collection<T> getCollection() {
        return ArrayUtils.toCollection(this.elements);
    }

    @Override
    public int _getCount() {
        return this.elements.length;
    }

    @Override
    public boolean _contains(T value) {
        return ArrayUtils.contains(this.elements, value);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        System.arraycopy(this.elements, 0, array, arrayIndex, this.elements.length);
    }

    @Override
    public T[] _toArray(Class<T> clazz) {
        return ArrayUtils.toArray(this.elements, clazz);
    }

    @Override
    public Object[] _toArray() {
        return ArrayUtils.toArray(this.elements, Object.class);
    }

    @Override
    public List<T> _toList() {
        return ArrayUtils.toList(this.elements);
    }

    @Override
    public Array<T> clone() {
        return new Array<>(ArrayUtils.clone(this.elements));
    }
}
