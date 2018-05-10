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
public final class Join {
    private Join() {
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        return join(outer, inner, outerKeySelector, innerKeySelector, resultSelector, null);
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

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        return leftJoin(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector) {
        return leftJoin(outer, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return leftJoin(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
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

        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        return rightJoin(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, Func2<TOuter, TInner, TResult> resultSelector) {
        return rightJoin(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return rightJoin(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
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

        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        return fullJoin(outer, inner, outerKeySelector, innerKeySelector, null, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector) {
        return fullJoin(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return fullJoin(outer, inner, outerKeySelector, innerKeySelector, null, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
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

        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector, comparer);
    }

    public static <TOuter, TInner, TResult> IEnumerable<TResult> crossJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null)
            throw Errors.argumentNull("outer");
        if (inner == null)
            throw Errors.argumentNull("inner");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        return new CrossJoinIterator<>(outer, inner, resultSelector);
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

    JoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
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


final class LeftJoinIterator<TOuter, TInner, TKey, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TOuter> outer;
    private final IEnumerable<TInner> inner;
    private final Func1<TOuter, TKey> outerKeySelector;
    private final Func1<TInner, TKey> innerKeySelector;
    private final TInner defaultInner;
    private final Func2<TOuter, TInner, TResult> resultSelector;
    private final IEqualityComparer<TKey> comparer;
    private Lookup<TKey, TInner> lookup;
    private IEnumerator<TOuter> outerEnumerator;
    private TOuter item;
    private Grouping<TKey, TInner> g;
    private int index;

    LeftJoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        this.outer = outer;
        this.inner = inner;
        this.outerKeySelector = outerKeySelector;
        this.innerKeySelector = innerKeySelector;
        this.defaultInner = defaultInner;
        this.resultSelector = resultSelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new LeftJoinIterator<>(this.outer, this.inner, this.outerKeySelector, this.innerKeySelector, this.defaultInner, this.resultSelector, this.comparer);
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
                    this.state = 2;
                case 2:
                    this.item = this.outerEnumerator.current();
                    this.g = this.lookup.fetchGrouping(this.outerKeySelector.apply(this.item));
                    if (this.g == null) {
                        this.current = this.resultSelector.apply(this.item, this.defaultInner);
                        this.state = 3;
                        return true;
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
    private Grouping<TKey, TOuter> g;
    private int index;

    RightJoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
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
                    this.innerEnumerator = this.inner.enumerator();
                    if (!this.innerEnumerator.moveNext()) {
                        this.close();
                        return false;
                    }
                    this.lookup = Lookup.createForJoin(this.outer, this.outerKeySelector, this.comparer);
                    this.state = 2;
                case 2:
                    this.item = this.innerEnumerator.current();
                    this.g = this.lookup.fetchGrouping(this.innerKeySelector.apply(this.item));
                    if (this.g == null) {
                        this.current = this.resultSelector.apply(this.defaultOuter, this.item);
                        this.state = 3;
                        return true;
                    }
                    this.index = -1;
                    this.state = 4;
                    break;
                case 3:
                    if (!this.innerEnumerator.moveNext()) {
                        this.close();
                        return false;
                    }
                    this.state = 2;
                    break;
                case 4:
                    this.index++;
                    if (this.index < this.g._getCount()) {
                        this.current = this.resultSelector.apply(this.g.get(this.index), this.item);
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
        if (this.innerEnumerator != null) {
            this.innerEnumerator.close();
            this.innerEnumerator = null;
        }
        this.item = null;
        this.g = null;
        super.close();
    }
}


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
    private IEnumerator<Grouping<TKey, TInner>> unfetchedGroupingEnumerator;
    private TOuter item;
    private Grouping<TKey, TInner> g;
    private int index;

    FullJoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
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
                    this.outerEnumerator = this.outer.enumerator();
                    this.lookup = Lookup.createForFullJoin(this.inner, this.innerKeySelector, this.comparer);
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
                    if (this.index < this.g._getCount()) {
                        this.current = this.resultSelector.apply(this.item, this.g.get(this.index));
                        return true;
                    }
                    this.state = 2;
                    break;
                case 5:
                    this.index++;
                    if (this.index < this.g._getCount()) {
                        this.current = this.resultSelector.apply(this.defaultOuter, this.g.get(this.index));
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


final class CrossJoinIterator<TOuter, TInner, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TOuter> outer;
    private final IEnumerable<TInner> inner;
    private final Func2<TOuter, TInner, TResult> resultSelector;
    private IEnumerator<TOuter> outerEnumerator;
    private IEnumerator<TInner> innerEnumerator;
    private TOuter item;

    CrossJoinIterator(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func2<TOuter, TInner, TResult> resultSelector) {
        this.outer = outer;
        this.inner = inner;
        this.resultSelector = resultSelector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new CrossJoinIterator<>(this.outer, this.inner, this.resultSelector);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.outerEnumerator = this.outer.enumerator();
                    this.state = 2;
                case 2:
                    if (this.outerEnumerator.moveNext()) {
                        this.item = this.outerEnumerator.current();
                        this.innerEnumerator = this.inner.enumerator();
                        this.state = 3;
                        break;
                    }
                    this.close();
                    return false;
                case 3:
                    if (this.innerEnumerator.moveNext()) {
                        this.current = this.resultSelector.apply(this.item, this.innerEnumerator.current());
                        return true;
                    }
                    this.innerEnumerator.close();
                    this.state = 2;
                    break;
                default:
                    return false;
            }
        } while (true);


    }

    @Override
    public void close() {
        if (this.outerEnumerator != null) {
            this.outerEnumerator.close();
            this.outerEnumerator = null;
        }
        if (this.innerEnumerator != null) {
            this.innerEnumerator.close();
            this.innerEnumerator = null;
        }
        this.item = null;
        super.close();
    }
}
