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
 * Created by 许崇雷 on 2018-05-04.
 */
public final class Skip {
    private Skip() {
    }

    public static <TSource> IEnumerable<TSource> skip(IEnumerable<TSource> source, int count) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (count <= 0) {
            if (source instanceof Iterator || source instanceof IPartition)
                return source;
            count = 0;
        } else if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return partition._skip(count);
        }

        if (source instanceof IList) {
            IList<TSource> sourceList = (IList<TSource>) source;
            return new ListPartition<>(sourceList, count, Integer.MAX_VALUE);
        }

        return new EnumerablePartition<>(source, count, -1);
    }

    public static <TSource> IEnumerable<TSource> skipWhile(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        return new SkipWhileIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> skipWhile(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        return new SkipWhileIterator2<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> skipLast(IEnumerable<TSource> source, int count) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (count <= 0)
            return source.skip(0);

        return new SkipLastIterator<>(source, count);
    }
}


final class SkipWhileIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private IEnumerator<TSource> enumerator;

    SkipWhileIterator(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
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
    private final Func2<TSource, Integer, Boolean> predicate;
    private IEnumerator<TSource> enumerator;

    SkipWhileIterator2(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
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


final class SkipLastIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final int count;
    private IEnumerator<TSource> enumerator;
    private Queue<TSource> queue;

    SkipLastIterator(IEnumerable<TSource> source, int count) {
        assert source != null;
        assert count > 0;
        this.source = source;
        this.count = count;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new SkipLastIterator<>(this.source, this.count);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.queue = new ArrayDeque<>();
                this.enumerator = this.source.enumerator();
                while (this.enumerator.moveNext()) {
                    if (this.queue.size() == this.count) {
                        this.current = this.queue.remove();
                        this.state = 2;
                        return true;
                    }
                    this.queue.add(this.enumerator.current());
                }
                this.close();
                return false;
            case 2:
                this.queue.add(this.enumerator.current());
                if (this.enumerator.moveNext()) {
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
