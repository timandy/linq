package com.bestvike.linq.debug;

/**
 * Created by 许崇雷 on 2019-12-06.
 */
public final class ObjectDebugView implements IDebugView {
    private final Object obj;

    ObjectDebugView(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object getProxyObject() {
        return this.obj;
    }
}
