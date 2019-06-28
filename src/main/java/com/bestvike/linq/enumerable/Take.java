package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.IndexPredicate2;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class Take {
    private Take() {
    }

    public static <TSource> IEnumerable<TSource> take(IEnumerable<TSource> source, int count) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (count <= 0)
            return EmptyPartition.instance();

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return partition._take(count);
        }

        if (source instanceof IList) {
            IList<TSource> sourceList = (IList<TSource>) source;
            return new ListPartition<>(sourceList, 0, count - 1);
        }

        return new EnumerablePartition<>(source, 0, count - 1);
    }

    public static <TSource> IEnumerable<TSource> takeWhile(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        return new TakeWhileIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> takeWhile(IEnumerable<TSource> source, IndexPredicate2<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        return new TakeWhileIterator2<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> takeLast(IEnumerable<TSource> source, int count) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (count <= 0)
            return EmptyPartition.instance();

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            int length = partition._getCount(true);
            if (length >= 0)
                return length - count > 0 ? partition.skip(length - count) : partition;
        } else if (source instanceof IList) {
            IList<TSource> sourceList = (IList<TSource>) source;
            int sourceCount = sourceList._getCount();
            return sourceCount > count
                    ? new ListPartition<>(sourceList, sourceCount - count, sourceCount)
                    : new ListPartition<>(sourceList, 0, sourceCount);
        }

        return new TakeLastIterator<>(source, count);
    }
}


final class TakeWhileIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Predicate1<TSource> predicate;
    private IEnumerator<TSource> enumerator;

    TakeWhileIterator(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new TakeWhileIterator<>(this.source, this.predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.predicate.apply(item)) {
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
        }
        super.close();
    }
}


final class TakeWhileIterator2<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final IndexPredicate2<TSource> predicate;
    private IEnumerator<TSource> enumerator;
    private int index;

    TakeWhileIterator2(IEnumerable<TSource> source, IndexPredicate2<TSource> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new TakeWhileIterator2<>(this.source, this.predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.index = -1;
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    this.index = Math.addExact(this.index, 1);
                    if (this.predicate.apply(item, this.index)) {
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
        }
        super.close();
    }
}


final class TakeLastIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final int count;
    private Queue<TSource> queue;

    TakeLastIterator(IEnumerable<TSource> source, int count) {
        assert source != null;
        assert count > 0;
        this.source = source;
        this.count = count;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new TakeLastIterator<>(this.source, this.count);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                try (IEnumerator<TSource> enumerator = this.source.enumerator()) {
                    if (!enumerator.moveNext()) {
                        this.close();
                        return false;
                    }
                    this.queue = new ArrayDeque<>();
                    this.queue.add(enumerator.current());
                    while (enumerator.moveNext()) {
                        if (this.queue.size() < this.count) {
                            this.queue.add(enumerator.current());
                            continue;
                        }
                        do {
                            this.queue.remove();
                            this.queue.add(enumerator.current());
                        } while (enumerator.moveNext());
                        break;
                    }
                }
                assert this.queue.size() <= this.count;
                this.current = this.queue.remove();
                this.state = 2;
                return true;
            case 2:
                if (this.queue.size() > 0) {
                    this.current = this.queue.remove();
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
        this.queue = null;
        super.close();
    }
}
