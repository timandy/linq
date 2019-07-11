package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.SingletonEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-04-10.
 */
public final class SingletonEnumerable<TSource> implements IList<TSource> {
    private final TSource element;

    public SingletonEnumerable(TSource element) {
        this.element = element;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        return new SingletonEnumerator<>(this.element);
    }

    @Override
    public TSource get(int index) {
        if (index == 0)
            return this.element;
        ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.index);
        return null;
    }

    @Override
    public int _indexOf(TSource item) {
        return Objects.equals(this.element, item) ? 0 : -1;
    }

    @Override
    public int _lastIndexOf(TSource item) {
        return Objects.equals(this.element, item) ? 0 : -1;
    }

    @Override
    public int _findIndex(Predicate1<TSource> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        return match.apply(this.element) ? 0 : -1;
    }

    @Override
    public int _findLastIndex(Predicate1<TSource> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        return match.apply(this.element) ? 0 : -1;
    }

    @Override
    public Collection<TSource> getCollection() {
        return ArrayUtils.toCollection(new Object[]{this.element});
    }

    @Override
    public int _getCount() {
        return 1;
    }

    @Override
    public boolean _contains(TSource item) {
        return Objects.equals(item, this.element);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        array[arrayIndex] = this.element;
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        return ArrayUtils.singleton(clazz, this.element);
    }

    @Override
    public Object[] _toArray() {
        return ArrayUtils.singleton(this.element);
    }

    @Override
    public List<TSource> _toList() {
        return ListUtils.singleton(this.element);
    }
}
