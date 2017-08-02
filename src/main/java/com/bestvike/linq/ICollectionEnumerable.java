package com.bestvike.linq;

import com.bestvike.linq.util.Array;

import java.util.List;

/**
 * Created by 许崇雷 on 2017/7/20.
 */
public interface ICollectionEnumerable<TElement> extends IEnumerable<TElement> {
    int internalSize();

    boolean internalContains(TElement value);

    Array<TElement> internalToArray();

    TElement[] internalToArray(Class<TElement> clazz);

    List<TElement> internalToList();
}
