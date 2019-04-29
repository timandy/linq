package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Spliterator;
import java.util.Spliterators;

/**
 * Created by 许崇雷 on 2019-04-25.
 */
public final class ToSpliterator {
    private ToSpliterator() {
    }

    public static <TSource> Spliterator<TSource> spliterator(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof ICollection) {
            if (source instanceof IArray) {
                IArray<TSource> array = (IArray<TSource>) source;
                Object arr = array.getArray();
                Class<?> componentType = arr.getClass().getComponentType();
                if (componentType.isPrimitive()) {
                    if (componentType == int.class)
                        //noinspection unchecked
                        return (Spliterator<TSource>) Spliterators.spliterator((int[]) arr, array._getStartIndex(), array._getEndIndex(), Spliterator.IMMUTABLE);
                    if (componentType == long.class)
                        //noinspection unchecked
                        return (Spliterator<TSource>) Spliterators.spliterator((long[]) arr, array._getStartIndex(), array._getEndIndex(), Spliterator.IMMUTABLE);
                    if (componentType == double.class)
                        //noinspection unchecked
                        return (Spliterator<TSource>) Spliterators.spliterator((double[]) arr, array._getStartIndex(), array._getEndIndex(), Spliterator.IMMUTABLE);
                    return Spliterators.spliterator(array._toArray(), array._getStartIndex(), array._getEndIndex(), Spliterator.IMMUTABLE);
                }
                return Spliterators.spliterator((Object[]) arr, array._getStartIndex(), array._getEndIndex(), Spliterator.IMMUTABLE);
            }

            ICollection<TSource> collection = (ICollection<TSource>) source;
            return Spliterators.spliterator(collection.getCollection(), Spliterator.IMMUTABLE);
        }

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProv = (IIListProvider<TSource>) source;
            int count = listProv._getCount(true);
            if (count != -1)
                return Spliterators.spliterator(source.enumerator(), count, Spliterator.IMMUTABLE);
        }

        return Spliterators.spliteratorUnknownSize(source.enumerator(), Spliterator.IMMUTABLE);
    }
}
