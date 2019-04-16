package com.bestvike.linq.bridge.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.bridge.enumerator.SingletonEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-10.
 */
public final class SingletonEnumerable<TElement> implements IList<TElement> {
    private final TElement element;

    public SingletonEnumerable(TElement element) {
        this.element = element;
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new SingletonEnumerator<>(this.element);
    }

    @Override
    public TElement get(int index) {
        if (index == 0)
            return this.element;
        throw Errors.argumentOutOfRange("index");
    }

    @Override
    public Collection<TElement> getCollection() {
        return ArrayUtils.toCollection(new Object[]{this.element});
    }

    @Override
    public int _getCount() {
        return 1;
    }

    @Override
    public boolean _contains(TElement item) {
        return Objects.equals(item, this.element);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        array[arrayIndex] = this.element;
    }

    @Override
    public TElement[] _toArray(Class<TElement> clazz) {
        return ArrayUtils.singleton(clazz, this.element);
    }

    @Override
    public Object[] _toArray() {
        return ArrayUtils.singleton(this.element);
    }

    @Override
    public List<TElement> _toList() {
        return ListUtils.singleton(this.element);
    }
}
