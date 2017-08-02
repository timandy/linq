package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.function.Func2;
import com.bestvike.linq.impl.Lookup;

/**
 * Created by 许崇雷 on 2017/7/24.
 */
final class RightJoinIterator<TOuter, TInner, TKey, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TOuter> outer;
    private final IEnumerable<TInner> inner;
    private final Func1<TOuter, TKey> outerKeySelector;
    private final Func1<TInner, TKey> innerKeySelector;
    private final TOuter defaultOuter;
    private final Func2<TOuter, TInner, TResult> resultSelector;
    private final IEqualityComparer<TKey> comparer;
    private Lookup<TKey, TOuter> lookup;
    private IEnumerator<TInner> innerEnumerator;
    private TInner item;
    private Lookup<TKey, TOuter>.Grouping g;
    private int index;

    public RightJoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        this.outer = outer;
        this.inner = inner;
        this.outerKeySelector = outerKeySelector;
        this.innerKeySelector = innerKeySelector;
        this.defaultOuter = defaultOuter;
        this.resultSelector = resultSelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new RightJoinIterator<>(this.outer, this.inner, this.outerKeySelector, this.innerKeySelector, this.defaultOuter, this.resultSelector, this.comparer);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.lookup = Lookup.createForJoin(this.outer, this.outerKeySelector, this.comparer);
                    this.innerEnumerator = this.inner.enumerator();
                    this.state = 2;
                case 2:
                    if (this.innerEnumerator.moveNext()) {
                        this.item = this.innerEnumerator.current();
                        this.g = this.lookup.fetchGrouping(this.innerKeySelector.apply(this.item));
                        if (this.g == null) {
                            this.current = this.resultSelector.apply(this.defaultOuter, this.item);
                            return true;
                        }
                        this.index = -1;
                        this.state = 3;
                        break;
                    }
                    this.close();
                    return false;
                case 3:
                    this.index++;
                    if (this.index < this.g.internalSize()) {
                        this.current = this.resultSelector.apply(this.g.internalGet(this.index), this.item);
                        return true;
                    }
                    this.state = 2;
                    break;
                default:
                    return false;
            }
        } while (true);
    }

    @Override
    public void close() {
        this.lookup = null;
        if (this.innerEnumerator != null) {
            this.innerEnumerator.close();
            this.innerEnumerator = null;
        }
        this.item = null;
        this.g = null;
        super.close();
    }
}
