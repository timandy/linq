package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.impl.grouped.Grouping;
import com.bestvike.linq.impl.grouped.Lookup;

/**
 * Created by 许崇雷 on 2018-05-02.
 */
public final class Join {
    private Join() {
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null)
            throw Errors.argumentNull("outer");
        if (inner == null)
            throw Errors.argumentNull("inner");
        if (outerKeySelector == null)
            throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null)
            throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        return new JoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null)
            throw Errors.argumentNull("outer");
        if (inner == null)
            throw Errors.argumentNull("inner");
        if (outerKeySelector == null)
            throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null)
            throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        return new JoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }
}


final class JoinIterator<TOuter, TInner, TKey, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TOuter> outer;
    private final IEnumerable<TInner> inner;
    private final Func1<TOuter, TKey> outerKeySelector;
    private final Func1<TInner, TKey> innerKeySelector;
    private final Func2<TOuter, TInner, TResult> resultSelector;
    private final IEqualityComparer<TKey> comparer;
    private Lookup<TKey, TInner> lookup;
    private IEnumerator<TOuter> outerEnumerator;
    private TOuter item;
    private Grouping<TKey, TInner> g;
    private int index;

    public JoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        this.outer = outer;
        this.inner = inner;
        this.outerKeySelector = outerKeySelector;
        this.innerKeySelector = innerKeySelector;
        this.resultSelector = resultSelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new JoinIterator<>(this.outer, this.inner, this.outerKeySelector, this.innerKeySelector, this.resultSelector, this.comparer);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.outerEnumerator = this.outer.enumerator();
                    if (!this.outerEnumerator.moveNext()) {
                        this.close();
                        return false;
                    }
                    this.lookup = Lookup.createForJoin(this.inner, this.innerKeySelector, this.comparer);
                    if (this.lookup.getCount() == 0) {
                        this.close();
                        return false;
                    }
                    this.state = 2;
                case 2:
                    this.item = this.outerEnumerator.current();
                    this.g = this.lookup.fetchGrouping(this.outerKeySelector.apply(this.item));
                    if (this.g == null) {
                        this.state = 3;
                        break;
                    }
                    this.index = -1;
                    this.state = 4;
                    break;
                case 3:
                    if (!this.outerEnumerator.moveNext()) {
                        this.close();
                        return false;
                    }
                    this.state = 2;
                    break;
                case 4:
                    this.index++;
                    if (this.index < this.g._getCount()) {
                        this.current = this.resultSelector.apply(this.item, this.g.get(this.index));
                        return true;
                    }
                    this.state = 3;
                    break;
                default:
                    return false;
            }
        } while (true);
    }

    @Override
    public void close() {
        this.lookup = null;
        if (this.outerEnumerator != null) {
            this.outerEnumerator.close();
            this.outerEnumerator = null;
        }
        this.item = null;
        this.g = null;
        super.close();
    }
}
