package com.bestvike.linq.debug;

import com.bestvike.linq.resources.SR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
final class IterableDebugView<TElement> implements IDebugView {
    private final Iterable<TElement> iterable;
    private Object[] cachedValues;

    IterableDebugView(Iterable<TElement> iterable) {
        this.iterable = iterable;
    }

    @Override
    public String getDebuggerDisplay() {
        return this.iterable.getClass().getName();
    }

    @Override
    public Object getDebuggerTypeProxy() {
        if (this.cachedValues == null) {
            List<TElement> list = new ArrayList<>();
            for (TElement element : this.iterable)
                list.add(element);
            this.cachedValues = list.toArray();
        }
        return this.cachedValues.length == 0
                ? SR.EmptyIterable
                : this.cachedValues;
    }
}
