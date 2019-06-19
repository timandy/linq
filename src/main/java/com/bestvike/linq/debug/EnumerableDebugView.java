package com.bestvike.linq.debug;

import com.bestvike.linq.IEnumerable;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
final class EnumerableDebugView<TElement> implements IDebugView {
    private final IEnumerable<TElement> enumerable;
    private Object[] cachedValues;

    EnumerableDebugView(IEnumerable<TElement> enumerable) {
        this.enumerable = enumerable;
    }

    @Override
    public String getDebuggerDisplay() {
        return this.enumerable.getClass().getName();
    }

    @Override
    public Object[] getDebuggerTypeProxy() {
        return this.cachedValues == null ?
                (this.cachedValues = this.enumerable.toArray().getArray())
                : this.cachedValues;
    }
}
