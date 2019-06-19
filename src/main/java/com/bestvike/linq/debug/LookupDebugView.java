package com.bestvike.linq.debug;

import com.bestvike.linq.ILookup;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
final class LookupDebugView<TKey, TElement> implements IDebugView {
    private final ILookup<TKey, TElement> lookup;
    private Object[] cachedGroupings;

    LookupDebugView(ILookup<TKey, TElement> lookup) {
        this.lookup = lookup;
    }

    @Override
    public String getDebuggerDisplay() {
        return "count = " + this.getDebuggerMemberString(this.lookup.getCount());
    }

    @Override
    public Object[] getDebuggerTypeProxy() {
        return this.cachedGroupings == null ?
                (this.cachedGroupings = this.lookup.toArray().getArray())
                : this.cachedGroupings;
    }
}
