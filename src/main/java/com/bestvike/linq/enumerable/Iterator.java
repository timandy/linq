package com.bestvike.linq.enumerable;

import com.bestvike.function.Func1;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;

/**
 * Created by 许崇雷 on 2017-07-10.
 */
public abstract class Iterator<TSource> extends AbstractIterator<TSource> {
    public <TResult> IEnumerable<TResult> _select(Func1<TSource, TResult> selector) {
        return new SelectEnumerableIterator<>(this, selector);
    }

    public IEnumerable<TSource> _where(Predicate1<TSource> predicate) {
        return new WhereEnumerableIterator<>(this, predicate);
    }
}
