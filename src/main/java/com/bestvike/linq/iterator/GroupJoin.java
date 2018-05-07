package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-05-02.
 */
public final class GroupJoin {
    private GroupJoin() {
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, IEnumerable<TInner>, TResult> resultSelector) {
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

        return new GroupJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, IEnumerable<TInner>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
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

        return new GroupJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }
}


final class GroupJoinIterator<TOuter, TInner, TKey, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TOuter> outer;
    private final IEnumerable<TInner> inner;
    private final Func1<TOuter, TKey> outerKeySelector;
    private final Func1<TInner, TKey> innerKeySelector;
    private final Func2<TOuter, IEnumerable<TInner>, TResult> resultSelector;
    private final IEqualityComparer<TKey> comparer;
    private Lookup<TKey, TInner> lookup;
    private IEnumerator<TOuter> outerEnumerator;

    GroupJoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, IEnumerable<TInner>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        this.outer = outer;
        this.inner = inner;
        this.outerKeySelector = outerKeySelector;
        this.innerKeySelector = innerKeySelector;
        this.resultSelector = resultSelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new GroupJoinIterator<>(this.outer, this.inner, this.outerKeySelector, this.innerKeySelector, this.resultSelector, this.comparer);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.outerEnumerator = this.outer.enumerator();
                if (!this.outerEnumerator.moveNext()) {
                    this.close();
                    return false;
                }
                this.lookup = Lookup.createForJoin(this.inner, this.innerKeySelector, this.comparer);
                TOuter item = this.outerEnumerator.current();
                this.current = this.resultSelector.apply(item, this.lookup.fetch(this.outerKeySelector.apply(item)));
                this.state = 2;
                return true;
            case 2:
                if (this.outerEnumerator.moveNext()) {
                    item = this.outerEnumerator.current();
                    this.current = this.resultSelector.apply(item, this.lookup.fetch(this.outerKeySelector.apply(item)));
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void close() {
        this.lookup = null;
        if (this.outerEnumerator != null) {
            this.outerEnumerator.close();
            this.outerEnumerator = null;
        }
        super.close();
    }
}
