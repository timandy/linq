package com.bestvike.linq.impl.order;

/**
 * Created by 许崇雷 on 2018-04-18.
 */
public abstract class AbstractCachingComparer<TElement> {
      abstract int compare(TElement element, boolean cacheLower);

      abstract void setElement(TElement element);
}
