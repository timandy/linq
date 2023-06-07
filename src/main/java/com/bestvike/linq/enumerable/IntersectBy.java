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
public final class IntersectBy {
    private IntersectBy() {
    }

    public static <TSource, TKey> IEnumerable<TSource> intersectBy(IEnumerable<TSource> first, IEnumerable<TKey> second, Func1<TSource, TKey> keySelector) {
        return intersectBy(first, second, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<TSource> intersectBy(IEnumerable<TSource> first, IEnumerable<TKey> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (first == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.first);
        if (second == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.second);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        return new IntersectByIterator<>(first, second, keySelector, comparer);
    }
}


final class IntersectByIterator<TSource, TKey> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> first;
    private final IEnumerable<TKey> second;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;
    private Set<TKey> set;
    private IEnumerator<TSource> enumerator;

    IntersectByIterator(IEnumerable<TSource> first, IEnumerable<TKey> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        this.first = first;
        this.second = second;
        this.keySelector = keySelector;
        this.comparer = comparer;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new IntersectByIterator<>(this.first, this.second, this.keySelector, this.comparer);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.set = new Set<>(this.second, this.comparer);
                this.enumerator = this.first.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.set.remove(this.keySelector.apply(item))) {
                        this.current = item;
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
