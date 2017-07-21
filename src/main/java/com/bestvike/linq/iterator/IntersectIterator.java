package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.impl.Set;

/**
 * @author 许崇雷
 * @date 2017/7/18
 */
public class IntersectIterator<TSource> extends AbstractIterator<TSource> {
    private IEnumerable<TSource> first;
    private IEnumerable<TSource> second;
    private IEqualityComparer<TSource> comparer;
    private Set<TSource> set;
    private IEnumerator<TSource> enumerator;

    public IntersectIterator(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        this.first = first;
        this.second = second;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new IntersectIterator<>(this.first, this.second, this.comparer);
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
            case 3:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.set.remove(item)) {
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
