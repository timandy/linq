package com.bestvike.linq.enumerable;

import com.bestvike.Index;
import com.bestvike.Range;
import com.bestvike.collections.generic.IArrayList;
import com.bestvike.collections.generic.IList;
import com.bestvike.collections.generic.Queue;
import com.bestvike.function.IndexPredicate2;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class Take {
    private Take() {
    }

    public static <TSource> IEnumerable<TSource> take(IEnumerable<TSource> source, int count) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return count <= 0
                ? EmptyPartition.instance()
                : takeIterator(source, count);
    }

    public static <TSource> IEnumerable<TSource> take(IEnumerable<TSource> source, Range range) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (range == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.range);

        Index start = range.getStart();
        Index end = range.getEnd();
        boolean isStartIndexFromEnd = start.isFromEnd();
        boolean isEndIndexFromEnd = end.isFromEnd();
        int startIndex = start.getValue();
        int endIndex = end.getValue();
        assert startIndex >= 0;
        assert endIndex >= 0;

        if (isStartIndexFromEnd) {
            if (startIndex == 0 || (isEndIndexFromEnd && endIndex >= startIndex)) {
                return EmptyPartition.instance();
            }
        } else if (!isEndIndexFromEnd) {
            return startIndex >= endIndex
                    ? EmptyPartition.instance()
                    : takeRangeIterator(source, startIndex, endIndex);
        }

        return new TakeRangeFromEndIterator<>(source, isStartIndexFromEnd, startIndex, isEndIndexFromEnd, endIndex);
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

        return count <= 0
                ? EmptyPartition.instance()
                : new TakeRangeFromEndIterator<>(source, true, count, true, 0);
    }

    static <TSource> IEnumerable<TSource> takeIterator(IEnumerable<TSource> source, int count) {
        assert source != null;
        assert count > 0;

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return partition._take(count);
        }

        if (source instanceof IList) {
            if (source instanceof IArrayList) {
                IArrayList<TSource> sourceList = (IArrayList<TSource>) source;
                return new ListPartition<>(sourceList, 0, count - 1);
            }

            IList<TSource> sourceList = (IList<TSource>) source;
            return new IListPartition<>(sourceList, 0, count - 1);
        }

        return new EnumerablePartition<>(source, 0, count - 1);
    }

    static <TSource> IEnumerable<TSource> takeRangeIterator(IEnumerable<TSource> source, int startIndex, int endIndex) {
        assert source != null;
        assert startIndex >= 0 && startIndex < endIndex;

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return takePartitionRange(partition, startIndex, endIndex);
        }

        if (source instanceof IList) {
            if (source instanceof IArrayList) {
                IArrayList<TSource> sourceList = (IArrayList<TSource>) source;
                return new ListPartition<>(sourceList, startIndex, endIndex - 1);
            }

            IList<TSource> sourceList = (IList<TSource>) source;
            return new IListPartition<>(sourceList, startIndex, endIndex - 1);
        }

        return new EnumerablePartition<>(source, startIndex, endIndex - 1);
    }

    static <TSource> IPartition<TSource> takePartitionRange(IPartition<TSource> partition, int startIndex, int endIndex) {
        partition = endIndex == 0 ? EmptyPartition.instance() : partition._take(endIndex);
        return startIndex == 0 ? partition : partition._skip(startIndex);
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


final class TakeRangeFromEndIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final boolean isStartIndexFromEnd;
    private final int startIndex;
    private final boolean isEndIndexFromEnd;
    private final int endIndex;
    private IEnumerator<TSource> enumerator;
    private Queue<TSource> queue;
    private int startIndexCalculated;
    private int endIndexCalculated;

    TakeRangeFromEndIterator(IEnumerable<TSource> source, boolean isStartIndexFromEnd, int startIndex, boolean isEndIndexFromEnd, int endIndex) {
        this.source = source;
        this.isStartIndexFromEnd = isStartIndexFromEnd;
        this.startIndex = startIndex;
        this.isEndIndexFromEnd = isEndIndexFromEnd;
        this.endIndex = endIndex;
    }

    private static int calculateStartIndex(boolean isStartIndexFromEnd, int startIndex, int count) {
        return Math.max(0, isStartIndexFromEnd ? count - startIndex : startIndex);
    }

    private static int calculateEndIndex(boolean isEndIndexFromEnd, int endIndex, int count) {
        return Math.min(count, isEndIndexFromEnd ? count - endIndex : endIndex);
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new TakeRangeFromEndIterator<>(this.source, this.isStartIndexFromEnd, this.startIndex, this.isEndIndexFromEnd, this.endIndex);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    assert this.source != null;
                    assert this.isStartIndexFromEnd || this.isEndIndexFromEnd;
                    assert this.isStartIndexFromEnd
                            ? this.startIndex > 0 && (!this.isEndIndexFromEnd || this.startIndex > this.endIndex)
                            : this.startIndex >= 0 && (this.isEndIndexFromEnd || this.startIndex < this.endIndex);
                    // Attempt to extract the count of the source enumerator,
                    // in order to convert fromEnd indices to regular indices.
                    // Enumerable counts can change over time, so it is very
                    // important that this check happens at enumeration time;
                    // do not move it outside of the iterator method.
                    out<Integer> countRef = out.init();
                    if (Count.tryGetNonEnumeratedCount(this.source, countRef)) {
                        int startIndexCalculated = calculateStartIndex(this.isStartIndexFromEnd, this.startIndex, countRef.value);
                        int endIndexCalculated = calculateEndIndex(this.isEndIndexFromEnd, this.endIndex, countRef.value);
                        if (startIndexCalculated < endIndexCalculated) {
                            this.enumerator = Take.takeRangeIterator(this.source, startIndexCalculated, endIndexCalculated).enumerator();
                            this.state = 2;
                            break;
                        }
                        this.close();
                        return false;
                    }
                    // start index is from end
                    if (this.isStartIndexFromEnd) {
                        int count;
                        try (IEnumerator<TSource> e = this.source.enumerator()) {
                            if (!e.moveNext()) {
                                this.close();
                                return false;
                            }
                            this.queue = new Queue<>();
                            this.queue.enqueue(e.current());
                            count = 1;
                            while (e.moveNext()) {
                                if (count < this.startIndex) {
                                    this.queue.enqueue(e.current());
                                    ++count;
                                } else {
                                    do {
                                        this.queue.dequeue();
                                        this.queue.enqueue(e.current());
                                        count = Math.addExact(count, 1);
                                    } while (e.moveNext());
                                    break;
                                }
                            }
                            assert this.queue.size() == Math.min(count, this.startIndex);
                        }
                        this.startIndexCalculated = calculateStartIndex(true, this.startIndex, count);
                        this.endIndexCalculated = calculateEndIndex(this.isEndIndexFromEnd, this.endIndex, count);
                        assert this.endIndexCalculated - this.startIndexCalculated <= this.queue.size();
                        this.state = 3;
                        break;
                    }
                    // start index not from end
                    assert !this.isStartIndexFromEnd && this.isEndIndexFromEnd;
                    this.enumerator = this.source.enumerator();
                    int count = 0;
                    while (count < this.startIndex && this.enumerator.moveNext()) {
                        count++;
                    }
                    if (count != this.startIndex) {
                        this.close();
                        return false;
                    }
                    this.queue = new Queue<>();
                    while (this.enumerator.moveNext()) {
                        if (this.queue.size() != this.endIndex) {
                            this.queue.enqueue(this.enumerator.current());
                            continue;
                        }
                        this.queue.enqueue(this.enumerator.current());
                        this.current = this.queue.dequeue();
                        this.state = 4;
                        return true;
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
                case 3:
                    if (this.startIndexCalculated < this.endIndexCalculated) {
                        this.startIndexCalculated++;
                        this.current = this.queue.dequeue();
                        return true;
                    }
                    this.close();
                    return false;
                case 4:
                    if (this.enumerator.moveNext()) {
                        this.queue.enqueue(this.enumerator.current());
                        this.current = this.queue.dequeue();
                        return true;
                    }
                    this.close();
                    return false;
                default:
                    return false;
            }
        } while (true);
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        if (this.queue != null) {
            this.queue.clear();
            this.queue = null;
        }
        this.startIndexCalculated = 0;
        this.endIndexCalculated = 0;
        super.close();
    }
}
