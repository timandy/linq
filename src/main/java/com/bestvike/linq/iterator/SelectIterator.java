package com.bestvike.linq.iterator;

import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
final class SelectIterator<TSource, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TSource> source;
    private final Func2<TSource, Integer, TResult> selector;
    private IEnumerator<TSource> enumerator;
    private int index;

    SelectIterator(IEnumerable<TSource> source, Func2<TSource, Integer, TResult> selector) {
        this.source = source;
        this.selector = selector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new SelectIterator<>(this.source, this.selector);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.index = -1;
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    this.index = Math.addExact(this.index, 1);
                    this.current = this.selector.apply(item, this.index);
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
