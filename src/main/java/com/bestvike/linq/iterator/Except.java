package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-04-28.
 */
public final class Except {
    private Except() {
    }

    public static <TSource> IEnumerable<TSource> except(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        return except(first, second, null);
    }

    public static <TSource> IEnumerable<TSource> except(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        if (first == null)
            throw Errors.argumentNull("first");
        if (second == null)
            throw Errors.argumentNull("second");

        return new ExceptIterator<>(first, second, comparer);
    }
}


final class ExceptIterator<TSource> extends AbstractIterator<TSource> {
    private final com.bestvike.linq.IEnumerable<TSource> first;
    private final IEnumerable<TSource> second;
    private final IEqualityComparer<TSource> comparer;
    private Set<TSource> set;
    private IEnumerator<TSource> enumerator;

    ExceptIterator(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        this.first = first;
        this.second = second;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new ExceptIterator<>(this.first, this.second, this.comparer);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.set = new Set<>(this.comparer);
                for (TSource item : this.second)
                    this.set.add(item);
                this.enumerator = this.first.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.set.add(item)) {
                        this.current = item;
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
        this.set = null;
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }
}