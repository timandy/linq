package com.bestvike.linq.impl.partition;

import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.iterator.AbstractIterator;
import com.bestvike.linq.iterator.Iterator;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;
import com.bestvike.out;

import java.util.List;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public final class EmptyPartition<TElement> extends Iterator<TElement> implements IPartition<TElement> {
    private EmptyPartition() {
    }

    public static <TElement> IPartition<TElement> instance() {
        return new EmptyPartition<>();
    }


    //region IEnumerator

    @Override
    public AbstractIterator<TElement> clone() {
        return this;
    }

    @Override
    public boolean moveNext() {
        return false;
    }

    //endregion


    //region IListProvider

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return 0;
    }

    @Override
    public TElement[] _toArray(Class<TElement> clazz) {
        return ArrayUtils.empty(clazz);
    }

    @Override
    public Array<TElement> _toArray() {
        return Array.empty();
    }

    @Override
    public List<TElement> _toList() {
        return ListUtils.empty();
    }

    //endregion


    //region IPartition

    @Override
    public IPartition<TElement> _skip(int count) {
        return this;
    }

    @Override
    public IPartition<TElement> _take(int count) {
        return this;
    }

    @Override
    public TElement _tryGetElementAt(int index, out<Boolean> found) {
        found.setValue(false);
        return null;
    }

    @Override
    public TElement _tryGetFirst(out<Boolean> found) {
        found.setValue(false);
        return null;
    }

    @Override
    public TElement _tryGetLast(out<Boolean> found) {
        found.setValue(false);
        return null;
    }

    //endregion


    //region Iterator

    @SuppressWarnings("unchecked")
    @Override
    public <TResult> IEnumerable<TResult> _select(Func1<TElement, TResult> selector) {
        return (IEnumerable<TResult>) this;
    }

    @Override
    public IEnumerable<TElement> _where(Func1<TElement, Boolean> predicate) {
        return this;
    }

    //endregion
}
