package com.bestvike.linq;

/**
 * @author 许崇雷
 * @date 2017/7/20
 */
public interface IListEnumerable<TElement> extends ICollectionEnumerable<TElement> {
    TElement internalGet(int index);
}
