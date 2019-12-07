package com.bestvike.linq.debug;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.resources.SR;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
public final class EnumerableDebugView<TElement> implements IDebugView {
    private final IEnumerable<TElement> enumerable;
    private Object[] cachedValues;

    EnumerableDebugView(IEnumerable<TElement> enumerable) {
        this.enumerable = enumerable;
    }

    @Override
    public Object getProxyObject() {
        if (this.cachedValues == null)
            this.cachedValues = this.enumerable.toArray().getArray();
        return this.cachedValues.length == 0
                ? SR.EmptyEnumerable
                : this.cachedValues;
    }
}
