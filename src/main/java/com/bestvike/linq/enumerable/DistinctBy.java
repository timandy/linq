package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
public final class DistinctBy {
    private DistinctBy() {
    }

    public static <TSource, TKey> IEnumerable<TSource> distinctBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return distinctBy(source, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<TSource> distinctBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        return new DistinctByIterator<>(source, keySelector, comparer);
    }
}


final class DistinctByIterator<TSource, TKey> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;
    private Set<TKey> set;
    private IEnumerator<TSource> enumerator;

    DistinctByIterator(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        this.source = source;
        this.keySelector = keySelector;
        this.comparer = comparer;
    }

    @Override
    public Iterator<TSource> clone() {
        return new DistinctByIterator<>(this.source, this.keySelector, this.comparer);
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
                this.set.add(this.keySelector.apply(element));
                this.current = element;
                this.state = 2;
                return true;
            case 2:
                while (this.enumerator.moveNext()) {
                    element = this.enumerator.current();
                    if (this.set.add(this.keySelector.apply(element))) {
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
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        Set<TKey> set = new Set<>(this.comparer);
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                if (set.add(this.keySelector.apply(element)))
                    builder.add(element);
            }
        }

        return builder.toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        Set<TKey> set = new Set<>(this.comparer);
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                if (set.add(this.keySelector.apply(element)))
                    builder.add(element);
            }
        }

        return builder.toArray();
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = new ArrayList<>();
        Set<TKey> set = new Set<>(this.comparer);
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                if (set.add(this.keySelector.apply(element)))
                    list.add(element);
            }
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : this.fillSet().getCount();
    }

    private Set<TKey> fillSet() {
        Set<TKey> set = new Set<>(this.comparer);
        set.unionWith(this.source, this.keySelector);
        return set;
    }
}
