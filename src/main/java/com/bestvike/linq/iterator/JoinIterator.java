package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.function.Func2;
import com.bestvike.linq.impl.Lookup;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
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
    private Lookup<TKey, TInner>.Grouping g;
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
                    this.lookup = Lookup.createForJoin(this.inner, this.innerKeySelector, this.comparer);
                    this.outerEnumerator = this.outer.enumerator();
                    this.state = 2;
                case 2:
                    while (this.outerEnumerator.moveNext()) {
                        this.item = this.outerEnumerator.current();
                        this.g = this.lookup.getGrouping(this.outerKeySelector.apply(this.item), false);
                        if (this.g != null) {
                            this.index = -1;
                            this.state = 3;
                            break;
                        }
                    }
                    if (this.state == 2) {
                        this.close();
                        return false;
                    }
                    break;
                case 3:
                    this.index++;
                    if (this.index < this.g.internalSize()) {
                        this.current = this.resultSelector.apply(this.item, this.g.internalGet(this.index));
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
        if (this.outerEnumerator != null) {
            this.outerEnumerator.close();
            this.outerEnumerator = null;
        }
        this.item = null;
        this.g = null;
        super.close();
    }
}
