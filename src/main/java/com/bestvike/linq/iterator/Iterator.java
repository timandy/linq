package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.function.Func1;

/**
 * Created by 许崇雷 on 2017/7/10.
 */
abstract class Iterator<TSource> extends AbstractIterator<TSource> {
    public abstract <TResult> IEnumerable<TResult> internalSelect(Func1<TSource, TResult> selector);

    public abstract IEnumerable<TSource> internalWhere(Func1<TSource, Boolean> predicate);
}
