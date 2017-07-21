package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.util.LambdaUtils;

/**
 * @author 许崇雷
 * @date 2017/7/16
 */

public class WhereEnumerableIterator<TSource> extends Iterator<TSource> {
    private IEnumerable<TSource> source;
    private Func1<TSource, Boolean> predicate;
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
    public <TResult> IEnumerable<TResult> select(Func1<TSource, TResult> selector) {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> where(Func1<TSource, Boolean> predicate) {
        return new WhereEnumerableIterator<>(this.source, LambdaUtils.CombinePredicates(this.predicate, predicate));
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
                break;
        }
        return false;
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