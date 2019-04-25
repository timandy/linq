package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Spliterator;
import java.util.Spliterators;

/**
 * Created by 许崇雷 on 2019-04-25.
 */
public final class Split {
    private Split() {
    }

    public static <TSource> Spliterator<TSource> spliterator(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            return Spliterators.spliterator(collection.getCollection(), Spliterator.IMMUTABLE);
        }

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProv = (IIListProvider<TSource>) source;
            final int count = listProv._getCount(true);
            if (count != -1)
                return Spliterators.spliterator(source.enumerator(), count, Spliterator.IMMUTABLE);
        }

        return Spliterators.spliteratorUnknownSize(source.enumerator(), Spliterator.IMMUTABLE);
    }
}
