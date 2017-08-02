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
final class FullJoinIterator<TOuter, TInner, TKey, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TOuter> outer;
    private final IEnumerable<TInner> inner;
    private final Func1<TOuter, TKey> outerKeySelector;
    private final Func1<TInner, TKey> innerKeySelector;
    private final TOuter defaultOuter;
    private final TInner defaultInner;
    private final Func2<TOuter, TInner, TResult> resultSelector;
    private final IEqualityComparer<TKey> comparer;
    private Lookup<TKey, TInner> lookup;
    private IEnumerator<TOuter> outerEnumerator;
    private IEnumerator<Lookup<TKey, TInner>.Grouping> unfetchedGroupingEnumerator;
    private TOuter item;
    private Lookup<TKey, TInner>.Grouping g;
    private int index;

    public FullJoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        this.outer = outer;
        this.inner = inner;
        this.outerKeySelector = outerKeySelector;
        this.innerKeySelector = innerKeySelector;
        this.defaultOuter = defaultOuter;
        this.defaultInner = defaultInner;
        this.resultSelector = resultSelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new FullJoinIterator<>(this.outer, this.inner, this.outerKeySelector, this.innerKeySelector, this.defaultOuter, this.defaultInner, this.resultSelector, this.comparer);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.lookup = Lookup.createForFullJoin(this.inner, this.innerKeySelector, this.comparer);
                    this.outerEnumerator = this.outer.enumerator();
                    this.state = 2;
                case 2:
                    if (this.outerEnumerator.moveNext()) {
                        this.item = this.outerEnumerator.current();
                        this.g = this.lookup.fetchGrouping(this.outerKeySelector.apply(this.item));
                        if (this.g == null) {
                            this.current = this.resultSelector.apply(this.item, this.defaultInner);
                            return true;
                        }
                        this.index = -1;
                        this.state = 4;
                        break;
                    }
                    this.outerEnumerator.close();
                    this.outerEnumerator = null;
                    this.unfetchedGroupingEnumerator = this.lookup.unfetchedEnumerator();
                    this.state = 3;
                case 3:
                    if (this.unfetchedGroupingEnumerator.moveNext()) {
                        this.g = this.unfetchedGroupingEnumerator.current();
                        this.index = -1;
                        this.state = 5;
                        break;
                    }
                    this.close();
                    return false;
                case 4:
                    this.index++;
                    if (this.index < this.g.internalSize()) {
                        this.current = this.resultSelector.apply(this.item, this.g.internalGet(this.index));
                        return true;
                    }
                    this.state = 2;
                    break;
                case 5:
                    this.index++;
                    if (this.index < this.g.internalSize()) {
                        this.current = this.resultSelector.apply(this.defaultOuter, this.g.internalGet(this.index));
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
        if (this.unfetchedGroupingEnumerator != null) {
            this.unfetchedGroupingEnumerator.close();
            this.unfetchedGroupingEnumerator = null;
        }
        this.item = null;
        this.g = null;
        super.close();
    }
}
