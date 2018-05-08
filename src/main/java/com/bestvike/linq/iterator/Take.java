package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

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
            throw Errors.argumentNull("source");

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

    public static <TSource> IEnumerable<TSource> takeWhile(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        return new TakeWhileIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> takeWhile(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        return new TakeWhileIterator2<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> takeLast(IEnumerable<TSource> source, int count) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (count <= 0)
            return EmptyPartition.instance();

        return new TakeLastIterator<>(source, count);
    }
}


final class TakeWhileIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private IEnumerator<TSource> enumerator;

    TakeWhileIterator(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
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
    private final Func2<TSource, Integer, Boolean> predicate;
    private IEnumerator<TSource> enumerator;
    private int index;

    TakeWhileIterator2(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
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
    private IEnumerator<TSource> enumerator;
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
                this.enumerator = this.source.enumerator();
                if (!this.enumerator.moveNext()) {
                    this.close();
                    return false;
                }
                this.queue = new ArrayDeque<>();
                this.queue.add(this.enumerator.current());
                while (this.enumerator.moveNext()) {
                    if (this.queue.size() < this.count) {
                        this.queue.add(this.enumerator.current());
                        continue;
                    }
                    do {
                        this.queue.remove();
                        this.queue.add(this.enumerator.current());
                    } while (this.enumerator.moveNext());
                    break;
                }
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
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        this.queue = null;
        super.close();
    }
}
