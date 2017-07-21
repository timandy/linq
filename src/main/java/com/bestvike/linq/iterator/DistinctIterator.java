package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.impl.Set;

/**
 * @author 许崇雷
 * @date 2017/7/18
 */
public class DistinctIterator<TSource> extends AbstractIterator<TSource> {
    private IEnumerable<TSource> source;
    private IEqualityComparer<TSource> comparer;
    private Set<TSource> set;
    private IEnumerator<TSource> enumerator;

    public DistinctIterator(IEnumerable<TSource> source, IEqualityComparer<TSource> comparer) {
        this.source = source;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new DistinctIterator<>(this.source, this.comparer);
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
