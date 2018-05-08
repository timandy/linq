package com.bestvike.linq.util;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2017/7/19.
 */
public final class ListUtils {
    private ListUtils() {
    }

    public static <T> List<T> empty() {
        return new ArrayList<>();
    }

    public static <T> List<T> singleton(T element) {
        List<T> list = new ArrayList<>();
        list.add(element);
        return list;
    }

    public static <T> void addRange(List<T> list, IEnumerable<T> enumerable) {
        if (list == null)
            throw Errors.argumentNull("list");
        if (enumerable == null)
            throw Errors.argumentNull("enumerable");

        if (enumerable instanceof ICollection) {
            ICollection<T> collection = (ICollection<T>) enumerable;
            list.addAll(collection.getCollection());
            return;
        }
        for (T item : enumerable)
            list.add(item);
    }
}
