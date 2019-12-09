package com.bestvike.linq.debug;

import com.bestvike.linq.ILookup;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
public final class LookupDebugView<TKey, TElement> implements IDebugView {
    private final ILookup<TKey, TElement> lookup;
    private Object[] cachedGroupings;

    LookupDebugView(ILookup<TKey, TElement> lookup) {
        this.lookup = lookup;
    }

    @Override
    public Object[] getProxyObject() {
        return this.cachedGroupings == null
                ? (this.cachedGroupings = this.lookup.toArray().getArray())
                : this.cachedGroupings;
    }
}
