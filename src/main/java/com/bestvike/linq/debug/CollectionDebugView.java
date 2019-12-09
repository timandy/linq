package com.bestvike.linq.debug;

import java.util.Collection;

/**
 * Created by 许崇雷 on 2019-12-09.
 */
public final class CollectionDebugView<TElement> implements IDebugView {
    private final Collection<TElement> collection;
    private Object[] cachedValues;

    CollectionDebugView(Collection<TElement> collection) {
        this.collection = collection;
    }

    @Override
    public Object getProxyObject() {
        return this.cachedValues == null
                ? (this.cachedValues = this.collection.toArray())
                : this.cachedValues;
    }
}
