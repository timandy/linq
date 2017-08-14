package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.impl.Set;

/**
 * Created by 许崇雷 on 2017/8/13.
 */
final class UnionByIterator<TSource, TKey> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> first;
    private final IEnumerable<TSource> second;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;
    private Set<TKey> set;
    private IEnumerator<TSource> enumerator;

    public UnionByIterator(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        this.first = first;
        this.second = second;
        this.keySelector = keySelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new UnionByIterator<>(this.first, this.second, this.keySelector, this.comparer);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.set = new Set<>(this.comparer);
                this.enumerator = this.first.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.set.add(this.keySelector.apply(item))) {
                        this.current = item;
                        return true;
                    }
                }
                this.enumerator.close();
                this.enumerator = this.second.enumerator();
                this.state = 3;
            case 3:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.set.add(this.keySelector.apply(item))) {
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
