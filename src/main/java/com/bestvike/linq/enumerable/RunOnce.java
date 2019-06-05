package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-11.
 */
public final class RunOnce {
    private RunOnce() {
    }

    public static <TSource> IEnumerable<TSource> runOnce(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return source instanceof IList
                ? new RunOnceList<>((IList<TSource>) source)
                : new RunOnceEnumerable<>(source);
    }
}


final class RunOnceEnumerable<TSource> implements IEnumerable<TSource> {
    private final IEnumerable<TSource> source;
    private boolean called;

    RunOnceEnumerable(IEnumerable<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwNotSupportedException();
        this.called = true;
        return this.source.enumerator();
    }
}


final class RunOnceList<TSource> implements IList<TSource> {
    private final IList<TSource> source;
    private final Set<Integer> called = new HashSet<>();

    RunOnceList(IList<TSource> source) {
        this.source = source;
    }

    private void assertAll() {
        if (!this.called.isEmpty())
            ThrowHelper.throwNotSupportedException();
        this.called.add(-1);
    }

    private void assertIndex(int index) {
        if (this.called.contains(-1))
            ThrowHelper.throwNotSupportedException();
        if (!this.called.add(index))
            ThrowHelper.throwNotSupportedException();
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        this.assertAll();
        return this.source.enumerator();
    }

    @Override
    public TSource get(int index) {
        this.assertIndex(index);
        return this.source.get(index);
    }

    @Override
    public Collection<TSource> getCollection() {
        this.assertAll();
        return this.source.getCollection();
    }

    @Override
    public int _getCount() {
        return this.source._getCount();
    }

    @Override
    public boolean _contains(TSource item) {
        this.assertAll();
        return this.source._contains(item);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        this.assertAll();
        this.source._copyTo(array, arrayIndex);
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        this.assertAll();
        return this.source._toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        this.assertAll();
        return this.source._toArray();
    }

    @Override
    public List<TSource> _toList() {
        this.assertAll();
        return this.source._toList();
    }
}
