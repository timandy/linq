package com.bestvike.linq;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public interface IGrouping<TKey, TElement> extends IEnumerable<TElement> {
    TKey getKey();
}
