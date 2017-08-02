package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.util.LambdaUtils;

/**
 * Created by 许崇雷 on 2017/7/16.
 */

final class WhereEnumerableIterator<TSource> extends Iterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private IEnumerator<TSource> enumerator;

    public WhereEnumerableIterator(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new WhereEnumerableIterator<>(this.source, this.predicate);
    }

    @Override
    public <TResult> IEnumerable<TResult> internalSelect(Func1<TSource, TResult> selector) {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> internalWhere(Func1<TSource, Boolean> predicate) {
        return new WhereEnumerableIterator<>(this.source, LambdaUtils.combinePredicates(this.predicate, predicate));
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
                    if (this.predicate.apply(item)) {
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
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }
}
