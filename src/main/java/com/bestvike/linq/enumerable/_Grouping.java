package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.bridge.enumerator.ArrayEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-04-28.
 */
final class Grouping<TKey, TElement> implements IGrouping<TKey, TElement>, IList<TElement> {
    TKey key;
    int hashCode;
    Object[] elements;
    int count;
    Grouping<TKey, TElement> hashNext;
    Grouping<TKey, TElement> next;
    boolean fetched;

    Grouping() {
    }

    void add(TElement element) {
        if (this.elements.length == this.count)
            this.elements = ArrayUtils.resize(this.elements, Math.multiplyExact(this.count, 2));
        this.elements[this.count] = element;
        this.count++;
    }

    public void trim() {
        if (this.elements.length != this.count)
            this.elements = ArrayUtils.resize(this.elements, this.count);
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new ArrayEnumerator<>(this.elements, 0, this.count);
    }

    @Override
    public TKey getKey() {
        return this.key;
    }

    @Override
    public TElement get(int index) {
        if (index < 0 || index >= this.count)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.index);
        //noinspection unchecked
        return (TElement) this.elements[index];
    }

    @Override
    public Collection<TElement> getCollection() {
        return ArrayUtils.toCollection(this.elements, 0, this.count);
    }

    @Override
    public int _getCount() {
        return this.count;
    }

    @Override
    public boolean _contains(TElement item) {
        return ArrayUtils.contains(this.elements, item, 0, this.count);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        System.arraycopy(this.elements, 0, array, arrayIndex, this.count);
    }

    @Override
    public TElement[] _toArray(Class<TElement> clazz) {
        return ArrayUtils.toArray(this.elements, clazz, 0, this.count);
    }

    @Override
    public Object[] _toArray() {
        return ArrayUtils.resize(this.elements, this.count);
    }

    @Override
    public List<TElement> _toList() {
        return ArrayUtils.toList(this.elements, 0, this.count);
    }
}
