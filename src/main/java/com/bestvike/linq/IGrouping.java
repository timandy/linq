package com.bestvike.linq;

/**
 * Created by 许崇雷 on 2017-07-11.
 */
public interface IGrouping<TKey, TElement> extends IEnumerable<TElement> {
    TKey getKey();
}
