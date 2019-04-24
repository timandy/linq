package com.bestvike.linq.util;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2017-07-19.
 */
public final class ListUtils {
    private ListUtils() {
    }

    public static <T> List<T> empty() {
        return new ArrayList<>(0);
    }

    public static <T> List<T> singleton(T element) {
        List<T> list = new ArrayList<>(1);
        list.add(element);
        return list;
    }

    public static <T> void addRange(List<T> list, IEnumerable<T> enumerable) {
        if (list == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.list);
        if (enumerable == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.enumerable);

        if (enumerable instanceof ICollection) {
            ICollection<T> collection = (ICollection<T>) enumerable;
            list.addAll(collection.getCollection());
            return;
        }
        for (T item : enumerable)
            list.add(item);
    }
}
