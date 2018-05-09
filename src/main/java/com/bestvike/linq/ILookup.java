package com.bestvike.linq;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public interface ILookup<TKey, TElement> extends IEnumerable<IGrouping<TKey, TElement>> {
    int getCount();

    IEnumerable<TElement> get(TKey key);

    boolean containsKey(TKey key);
}
