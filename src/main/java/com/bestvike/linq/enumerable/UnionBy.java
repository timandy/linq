package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
public final class UnionBy {
    private UnionBy() {
    }

    public static <TSource, TKey> IEnumerable<TSource> unionBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector) {
        return unionBy(first, second, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<TSource> unionBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (first == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.first);
        if (second == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.second);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        return new UnionByIterator<>(first, second, keySelector, comparer);
    }
}


final class UnionByIterator<TSource, TKey> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> first;
    private final IEnumerable<TSource> second;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;
    private Set<TKey> set;
    private IEnumerator<TSource> enumerator;

    UnionByIterator(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        this.first = first;
        this.second = second;
        this.keySelector = keySelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new UnionByIterator<>(this.first, this.second, this.keySelector, this.comparer);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.set = new Set<>(this.comparer);
                this.enumerator = this.first.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource element = this.enumerator.current();
                    if (this.set.add(this.keySelector.apply(element))) {
                        this.current = element;
                        return true;
                    }
                }
                this.enumerator.close();
                this.enumerator = this.second.enumerator();
                this.state = 3;
            case 3:
                while (this.enumerator.moveNext()) {
                    TSource element = this.enumerator.current();
                    if (this.set.add(this.keySelector.apply(element))) {
                        this.current = element;
                        return true;
                    }
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
            this.set = null;
        }
        super.close();
    }
}
