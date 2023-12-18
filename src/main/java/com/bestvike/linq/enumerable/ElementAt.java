package com.bestvike.linq.enumerable;

import com.bestvike.Index;
import com.bestvike.collections.generic.IList;
import com.bestvike.collections.generic.Queue;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class ElementAt {
    private ElementAt() {
    }

    public static <TSource> TSource elementAt(IEnumerable<TSource> source, int index) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            out<Boolean> foundRef = out.init();
            TSource element = partition._tryGetElementAt(index, foundRef);
            if (foundRef.value)
                return element;
        } else if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            return list.get(index);
        } else {
            out<TSource> elementRef = out.init();
            if (tryGetElement(source, index, elementRef))
                return elementRef.value;
        }
        ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.index);
        return null;
    }

    public static <TSource> TSource elementAt(IEnumerable<TSource> source, Index index) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (!index.isFromEnd())
            return elementAt(source, index.getValue());

        out<Integer> countRef = out.init();
        if (Count.tryGetNonEnumeratedCount(source, countRef))
            return elementAt(source, countRef.value - index.getValue());

        out<TSource> elementRef = out.init();
        if (!tryGetElementFromEnd(source, index.getValue(), elementRef))
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.index);

        return elementRef.value;
    }

    public static <TSource> TSource elementAtOrDefault(IEnumerable<TSource> source, int index) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            out<Boolean> foundRef = out.init();
            return partition._tryGetElementAt(index, foundRef);
        } else if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            return index >= 0 && index < list._getCount() ? list.get(index) : null;
        }

        out<TSource> elementRef = out.init();
        tryGetElement(source, index, elementRef);
        return elementRef.value;
    }

    public static <TSource> TSource elementAtOrDefault(IEnumerable<TSource> source, Index index) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (!index.isFromEnd())
            return elementAtOrDefault(source, index.getValue());

        out<Integer> countRef = out.init();
        if (Count.tryGetNonEnumeratedCount(source, countRef))
            return elementAtOrDefault(source, countRef.value - index.getValue());

        out<TSource> elementRef = out.init();
        tryGetElementFromEnd(source, index.getValue(), elementRef);
        return elementRef.value;
    }

    private static <TSource> boolean tryGetElement(IEnumerable<TSource> source, int index, out<TSource> element) {
        assert source != null;

        if (index >= 0) {
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext()) {
                    if (index == 0) {
                        element.value = e.current();
                        return true;
                    }
                    index--;
                }
            }
        }
        element.value = null;
        return false;
    }

    private static <TSource> boolean tryGetElementFromEnd(IEnumerable<TSource> source, int indexFromEnd, out<TSource> element) {
        assert source != null;
        if (indexFromEnd > 0) {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) {
                    Queue<TSource> queue = new Queue<>();
                    queue.enqueue(e.current());
                    while (e.moveNext()) {
                        if (queue.size() == indexFromEnd) {
                            queue.dequeue();
                        }
                        queue.enqueue(e.current());
                    }

                    if (queue.size() == indexFromEnd) {
                        element.value = queue.dequeue();
                        return true;
                    }
                }
            }
        }

        element.value = null;
        return false;
    }
}
