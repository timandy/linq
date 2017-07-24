package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.util.LambdaUtils;

/**
 * @author 许崇雷
 * @date 2017/7/16
 */
final class WhereSelectEnumerableIterator<TSource, TResult> extends Iterator<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private final Func1<TSource, TResult> selector;
    private IEnumerator<TSource> enumerator;

    public WhereSelectEnumerableIterator(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate, Func1<TSource, TResult> selector) {
        this.source = source;
        this.predicate = predicate;
        this.selector = selector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, this.selector);
    }

    @Override
    public <TResult2> IEnumerable<TResult2> internalSelect(Func1<TResult, TResult2> selector) {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, LambdaUtils.CombineSelectors(this.selector, selector));
    }

    @Override
    public IEnumerable<TResult> internalWhere(Func1<TResult, Boolean> predicate) {
        return new WhereEnumerableIterator<>(this, predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.predicate == null || this.predicate.apply(item)) {
                        this.current = this.selector.apply(item);
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
        }
        super.close();
    }
}
