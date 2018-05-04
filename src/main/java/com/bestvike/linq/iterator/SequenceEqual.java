package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
public final class SequenceEqual {
    private SequenceEqual() {
    }

    public static <TSource> boolean sequenceEqual(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        return sequenceEqual(first, second, null);
    }

    public static <TSource> boolean sequenceEqual(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        if (comparer == null)
            comparer = EqualityComparer.Default();
        if (first == null)
            throw Errors.argumentNull("first");
        if (second == null)
            throw Errors.argumentNull("second");

        if (first instanceof ICollection && second instanceof ICollection) {
            ICollection<TSource> firstCol = (ICollection<TSource>) first;
            ICollection<TSource> secondCol = (ICollection<TSource>) second;
            if (firstCol._getCount() != secondCol._getCount())
                return false;

            if (firstCol instanceof IList && secondCol instanceof IList) {
                IList<TSource> firstList = (IList<TSource>) first;
                IList<TSource> secondList = (IList<TSource>) second;
                int count = firstCol._getCount();
                for (int i = 0; i < count; i++) {
                    if (!comparer.equals(firstList.get(i), secondList.get(i)))
                        return false;
                }

                return true;
            }
        }

        try (IEnumerator<TSource> e1 = first.enumerator();
             IEnumerator<TSource> e2 = second.enumerator()) {
            while (e1.moveNext()) {
                if (!(e2.moveNext() && comparer.equals(e1.current(), e2.current())))
                    return false;
            }
            return !e2.moveNext();
        }
    }
}
