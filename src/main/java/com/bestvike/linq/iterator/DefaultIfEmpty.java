package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class DefaultIfEmpty {
    private DefaultIfEmpty() {
    }

    public static <TSource> IEnumerable<TSource> defaultIfEmpty(IEnumerable<TSource> source) {
        return defaultIfEmpty(source, null);
    }

    public static <TSource> IEnumerable<TSource> defaultIfEmpty(IEnumerable<TSource> source, TSource defaultValue) {
        if (source == null)
            throw Errors.argumentNull("source");

        return new DefaultIfEmptyIterator<>(source, defaultValue);
    }
}


final class DefaultIfEmptyIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final IEnumerable<TSource> source;
    private final TSource defaultValue;
    private IEnumerator<TSource> enumerator;

    DefaultIfEmptyIterator(IEnumerable<TSource> source, TSource defaultValue) {
        assert source != null;
        this.source = source;
        this.defaultValue = defaultValue;
    }

    @Override
    public Iterator<TSource> clone() {
        return new DefaultIfEmptyIterator<>(this.source, this.defaultValue);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
                    this.state = 2;
                    return true;
                }
                this.current = this.defaultValue;
                this.state = 3;
                return true;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
                    return true;
                }
                this.state = 3;
            case 3:
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        TSource[] array = this.source.toArray(clazz);
        return array.length == 0 ? ArrayUtils.singleton(clazz, this.defaultValue) : array;
    }

    @Override
    public Array<TSource> _toArray() {
        Array<TSource> array = this.source.toArray();
        return array.length() == 0 ? Array.singleton(this.defaultValue) : array;
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = this.source.toList();
        if (list.isEmpty())
            list.add(this.defaultValue);
        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        int count;
        if (!onlyIfCheap || this.source instanceof ICollection)
            count = this.source.count();
        else
            count = this.source instanceof IIListProvider ? ((IIListProvider<TSource>) this.source)._getCount(true) : -1;
        return count == 0 ? 1 : count;
    }
}
