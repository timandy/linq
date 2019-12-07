package com.bestvike.linq.debug;

import com.bestvike.linq.IGrouping;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
public final class GroupingDebugView<TKey, TElement> implements IDebugView {
    private final IGrouping<TKey, TElement> grouping;
    private Object[] cachedValues;

    GroupingDebugView(IGrouping<TKey, TElement> grouping) {
        this.grouping = grouping;
    }

    @Override
    public Object[] getProxyObject() {
        return this.cachedValues == null ?
                (this.cachedValues = this.grouping.toArray().getArray())
                : this.cachedValues;
    }
}
