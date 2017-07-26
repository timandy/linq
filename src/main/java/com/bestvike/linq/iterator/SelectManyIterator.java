package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func1;

/**
 * @author 许崇雷
 * @date 2017/7/16
 */
final class SelectManyIterator<TSource, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, IEnumerable<TResult>> selector;
    private IEnumerator<TSource> enumerator;
    private IEnumerator<TResult> selectorEnumerator;

    public SelectManyIterator(IEnumerable<TSource> source, Func1<TSource, IEnumerable<TResult>> selector) {
        this.source = source;
        this.selector = selector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new SelectManyIterator<>(this.source, this.selector);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.enumerator = this.source.enumerator();
                    this.state = 2;
                case 2:
                    if (this.enumerator.moveNext()) {
                        TSource item = this.enumerator.current();
                        this.selectorEnumerator = this.selector.apply(item).enumerator();
                        this.state = 3;
                        break;
                    }
                    this.close();
                    return false;
                case 3:
                    if (this.selectorEnumerator.moveNext()) {
                        this.current = this.selectorEnumerator.current();
                        return true;
                    }
                    this.selectorEnumerator.close();
                    this.state = 2;
                    break;
                default:
                    return false;
            }
        } while (true);
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        if (this.selectorEnumerator != null) {
            this.selectorEnumerator.close();
            this.selectorEnumerator = null;
        }
        super.close();
    }
}
