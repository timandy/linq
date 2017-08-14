package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.impl.Set;

/**
 * Created by 许崇雷 on 2017/8/13.
 */
final class DistinctByIterator<TSource, TKey> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;
    private Set<TKey> set;
    private IEnumerator<TSource> enumerator;

    public DistinctByIterator(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        this.source = source;
        this.keySelector = keySelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new DistinctByIterator<>(this.source, this.keySelector, this.comparer);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.set = new Set<>(this.comparer);
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
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
