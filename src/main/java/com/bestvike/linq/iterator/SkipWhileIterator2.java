package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func2;

/**
 * Created by 许崇雷 on 2017/7/17.
 */
final class SkipWhileIterator2<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Func2<TSource, Integer, Boolean> predicate;
    private IEnumerator<TSource> enumerator;

    public SkipWhileIterator2(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new SkipWhileIterator2<>(this.source, this.predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                int index = -1;
                this.enumerator = this.source.enumerator();
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    index = Math.addExact(index, 1);
                    if (!this.predicate.apply(item, index)) {
                        this.current = item;
                        this.state = 2;
                        return true;
                    }
                }
                this.close();
                return false;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
                    return true;
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
        }
        super.close();
    }
}
