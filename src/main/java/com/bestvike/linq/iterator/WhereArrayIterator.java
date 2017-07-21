package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.util.Array;
import com.bestvike.linq.util.LambdaUtils;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
public class WhereArrayIterator<TSource> extends Iterator<TSource> {
    private Array<TSource> source;
    private Func1<TSource, Boolean> predicate;
    private int index;

    public WhereArrayIterator(Array<TSource> source, Func1<TSource, Boolean> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new WhereArrayIterator<>(this.source, this.predicate);
    }

    @Override
    public <TResult> IEnumerable<TResult> select(Func1<TSource, TResult> selector) {
        return new WhereSelectArrayIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> where(Func1<TSource, Boolean> predicate) {
        return new WhereArrayIterator<>(this.source, LambdaUtils.CombinePredicates(this.predicate, predicate));
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                while (this.index < this.source.length()) {
                    TSource item = this.source.get(this.index);
                    this.index++;
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
        super.close();
    }
}
