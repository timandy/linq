package com.bestvike.linq;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public interface ILookup<TKey, TElement> extends IEnumerable<IGrouping<TKey, TElement>> {
    IEnumerable<TElement> get(TKey key);

    boolean containsKey(TKey key);
}
