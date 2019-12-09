package com.bestvike.linq.debug;

import java.util.Map;

/**
 * Created by 许崇雷 on 2019-12-09.
 */
public final class MapDebugView<TKey, TValue> implements IDebugView {
    private final Map<TKey, TValue> map;
    private Object[] cachedValues;

    MapDebugView(Map<TKey, TValue> map) {
        this.map = map;
    }

    @Override
    public Object getProxyObject() {
        return this.cachedValues == null
                ? (this.cachedValues = this.map.entrySet().toArray())
                : this.cachedValues;
    }
}
