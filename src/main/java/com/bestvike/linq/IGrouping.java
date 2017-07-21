package com.bestvike.linq;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public interface IGrouping<TKey, TElement> extends IEnumerable<TElement> {
    TKey getKey();
}
