package com.bestvike.linq;

/**
 * Created by 许崇雷 on 2017/7/20.
 */
public interface IListEnumerable<TElement> extends ICollectionEnumerable<TElement> {
    TElement internalGet(int index);
}
