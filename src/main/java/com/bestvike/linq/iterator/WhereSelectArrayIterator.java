package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.util.Array;
import com.bestvike.linq.util.LambdaUtils;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
public class WhereSelectArrayIterator<TSource, TResult> extends Iterator<TResult> {
    private final Array<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private final Func1<TSource, TResult> selector;
    private int index;

    public WhereSelectArrayIterator(Array<TSource> source, Func1<TSource, Boolean> predicate, Func1<TSource, TResult> selector) {
        this.source = source;
        this.predicate = predicate;
        this.selector = selector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new WhereSelectArrayIterator<>(this.source, this.predicate, this.selector);
    }

    @Override
    public <TResult2> IEnumerable<TResult2> select(Func1<TResult, TResult2> selector) {
        return new WhereSelectArrayIterator<>(this.source, this.predicate, LambdaUtils.CombineSelectors(this.selector, selector));
    }

    @Override
    public IEnumerable<TResult> where(Func1<TResult, Boolean> predicate) {
        return new WhereEnumerableIterator<>(this, predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                while (this.index < this.source.length()) {
                    TSource item = this.source.get(this.index);
                    this.index++;
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
}
