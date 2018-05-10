package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class Distinct {
    private Distinct() {
    }

    public static <TSource> IEnumerable<TSource> distinct(IEnumerable<TSource> source) {
        return distinct(source, null);
    }

    public static <TSource> IEnumerable<TSource> distinct(IEnumerable<TSource> source, IEqualityComparer<TSource> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");

        return new DistinctIterator<>(source, comparer);
    }
}


final class DistinctIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final IEnumerable<TSource> source;
    private final IEqualityComparer<TSource> comparer;
    private Set<TSource> set;
    private IEnumerator<TSource> enumerator;

    DistinctIterator(IEnumerable<TSource> source, IEqualityComparer<TSource> comparer) {
        assert source != null;
        this.source = source;
        this.comparer = comparer;
    }

    @Override
    public Iterator<TSource> clone() {
        return new DistinctIterator<>(this.source, this.comparer);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                if (!this.enumerator.moveNext()) {
                    this.close();
                    return false;
                }
                TSource element = this.enumerator.current();
                this.set = new Set<>(this.comparer);
                this.set.add(element);
                this.current = element;
                this.state = 2;
                return true;
            case 2:
                while (this.enumerator.moveNext()) {
                    element = this.enumerator.current();
                    if (this.set.add(element)) {
                        this.current = element;
                        return true;
                    }
                }
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
            this.set = null;
        }
        super.close();
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        return this.fillSet().toArray(clazz);
    }

    @Override
    public Array<TSource> _toArray() {
        return this.fillSet().toArray();
    }

    @Override
    public List<TSource> _toList() {
        return this.fillSet().toList();
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : this.fillSet().getCount();
    }

    private Set<TSource> fillSet() {
        Set<TSource> set = new Set<>(this.comparer);
        set.unionWith(this.source);
        return set;
    }
}