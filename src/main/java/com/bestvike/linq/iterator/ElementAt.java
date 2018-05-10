package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class ElementAt {
    private ElementAt() {
    }

    public static <TSource> TSource elementAt(IEnumerable<TSource> source, int index) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            out<Boolean> foundRef = out.init();
            TSource element = partition._tryGetElementAt(index, foundRef);
            if (foundRef.getValue())
                return element;
        } else {
            if (source instanceof IList) {
                IList<TSource> list = (IList<TSource>) source;
                return list.get(index);
            }

            if (index >= 0) {
                try (IEnumerator<TSource> e = source.enumerator()) {
                    while (e.moveNext()) {
                        if (index == 0)
                            return e.current();
                        index--;
                    }
                }
            }
        }
        throw Errors.argumentOutOfRange("index");
    }

    public static <TSource> TSource elementAtOrDefault(IEnumerable<TSource> source, int index) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            out<Boolean> foundRef = out.init();
            return partition._tryGetElementAt(index, foundRef);
        }

        if (index >= 0) {
            if (source instanceof IList) {
                IList<TSource> list = (IList<TSource>) source;
                if (index < list._getCount())
                    return list.get(index);
            } else {
                try (IEnumerator<TSource> e = source.enumerator()) {
                    while (e.moveNext()) {
                        if (index == 0)
                            return e.current();
                        index--;
                    }
                }
            }
        }
        return null;
    }
}
