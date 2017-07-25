package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func2;

/**
 * @author 许崇雷
 * @date 2017/7/24
 */
final class CrossJoinIterator<TOuter, TInner, TKey, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TOuter> outer;
    private final IEnumerable<TInner> inner;
    private final Func2<TOuter, TInner, TResult> resultSelector;
    private IEnumerator<TOuter> outerEnumerator;
    private IEnumerator<TInner> innerEnumerator;
    private TOuter item;

    public CrossJoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func2<TOuter, TInner, TResult> resultSelector) {
        this.outer = outer;
        this.inner = inner;
        this.resultSelector = resultSelector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new CrossJoinIterator<>(this.outer, this.inner, this.resultSelector);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.outerEnumerator = this.outer.enumerator();
                    this.state = 2;
                case 2:
                    if (this.outerEnumerator.moveNext()) {
                        this.item = this.outerEnumerator.current();
                        this.innerEnumerator = this.inner.enumerator();
                        this.state = 3;
                        break;
                    }
                    this.close();
                    return false;
                case 3:
                    if (this.innerEnumerator.moveNext()) {
                        this.current = this.resultSelector.apply(this.item, this.innerEnumerator.current());
                        return true;
                    }
                    this.innerEnumerator.close();
                    this.state = 2;
                    break;
                default:
                    return false;
            }
        } while (true);


    }

    @Override
    public void close() {
        if (this.outerEnumerator != null) {
            this.outerEnumerator.close();
            this.outerEnumerator = null;
        }
        if (this.innerEnumerator != null) {
            this.innerEnumerator.close();
            this.innerEnumerator = null;
        }
        this.item = null;
        super.close();
    }
}
