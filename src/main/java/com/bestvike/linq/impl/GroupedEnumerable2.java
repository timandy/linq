package com.bestvike.linq.impl;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.function.Func2;

/**
 * @author 许崇雷
 * @date 2017/7/12
 */
public class GroupedEnumerable2<TSource, TKey, TElement, TResult> implements IEnumerable<TResult> {
    private IEnumerable<TSource> source;
    private Func1<TSource, TKey> keySelector;
    private Func1<TSource, TElement> elementSelector;
    private IEqualityComparer<TKey> comparer;
    private Func2<TKey, IEnumerable<TElement>, TResult> resultSelector;

    public GroupedEnumerable2(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (elementSelector == null)
            throw Errors.argumentNull("elementSelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");
        this.source = source;
        this.keySelector = keySelector;
        this.elementSelector = elementSelector;
        this.comparer = comparer;
        this.resultSelector = resultSelector;
    }

    @Override
    public IEnumerator<TResult> enumerator() {
        Lookup<TKey, TElement> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup.applyResultSelector(this.resultSelector).enumerator();
    }
}