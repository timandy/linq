package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2018-05-02.
 */
public final class GroupJoin {
    private GroupJoin() {
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, IEnumerable<TInner>, TResult> resultSelector) {
        return groupJoin(outer, inner, outerKeySelector, innerKeySelector, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, IEnumerable<TInner>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.outer);
        if (inner == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.inner);
        if (outerKeySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.outerKeySelector);
        if (innerKeySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.innerKeySelector);
        if (resultSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.resultSelector);

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
    private IEnumerator<TOuter> outerEnumerator;
    private Lookup<TKey, TInner> lookup;

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
        TOuter item;
        switch (this.state) {
            case 1:
                this.outerEnumerator = this.outer.enumerator();
                if (!this.outerEnumerator.moveNext()) {
                    this.close();
                    return false;
                }
                this.lookup = Lookup.createForJoin(this.inner, this.innerKeySelector, this.comparer);
                item = this.outerEnumerator.current();
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
        if (this.outerEnumerator != null) {
            this.outerEnumerator.close();
            this.outerEnumerator = null;
            this.lookup = null;
        }
        super.close();
    }
}
