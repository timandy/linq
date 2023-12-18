package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IArrayList;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.IndexPredicate2;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
public final class Skip {
    private Skip() {
    }

    public static <TSource> IEnumerable<TSource> skip(IEnumerable<TSource> source, int count) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (count <= 0) {
            if (source instanceof Iterator || source instanceof IPartition)
                return source;
            count = 0;
        } else if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return partition._skip(count);
        }

        return skipIterator(source, count);
    }

    public static <TSource> IEnumerable<TSource> skipWhile(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        return new SkipWhileIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> skipWhile(IEnumerable<TSource> source, IndexPredicate2<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        return new SkipWhileIterator2<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> skipLast(IEnumerable<TSource> source, int count) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return count <= 0
                ? source.skip(0)
                : new TakeRangeFromEndIterator<>(source, false, 0, true, count);
    }

    private static <TSource> IEnumerable<TSource> skipIterator(IEnumerable<TSource> source, int count) {
        assert source != null;
        assert count >= 0;

        if (source instanceof IList) {
            if (source instanceof IArrayList) {
                IArrayList<TSource> sourceList = (IArrayList<TSource>) source;
                return new ListPartition<>(sourceList, count, Integer.MAX_VALUE);
            }

            IList<TSource> sourceList = (IList<TSource>) source;
            return new IListPartition<>(sourceList, count, Integer.MAX_VALUE);
        }

        return new EnumerablePartition<>(source, count, -1);
    }
}


final class SkipWhileIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Predicate1<TSource> predicate;
    private IEnumerator<TSource> enumerator;

    SkipWhileIterator(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        assert source != null;
        assert predicate != null;
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new SkipWhileIterator<>(this.source, this.predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (!this.predicate.apply(item)) {
                        this.current = item;
                        this.state = 2;
                        return true;
                    }
                }
                this.close();
                return false;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
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
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }
}


final class SkipWhileIterator2<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final IndexPredicate2<TSource> predicate;
    private IEnumerator<TSource> enumerator;

    SkipWhileIterator2(IEnumerable<TSource> source, IndexPredicate2<TSource> predicate) {
        assert source != null;
        assert predicate != null;
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new SkipWhileIterator2<>(this.source, this.predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                int index = -1;
                this.enumerator = this.source.enumerator();
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    index = Math.addExact(index, 1);
                    if (!this.predicate.apply(item, index)) {
                        this.current = item;
                        this.state = 2;
                        return true;
                    }
                }
                this.close();
                return false;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
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
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }
}
