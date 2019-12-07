package com.bestvike.linq.debug;

import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
public final class DebugView {
    private static final Map<Object, IDebugView> DEBUG_VIEW_MAP = new ConcurrentHashMap<>();

    private DebugView() {
    }

    public static String getDebuggerDisplay(Object obj) {
        if (obj == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.obj);

        return Debugger.getDebuggerDisplay(obj);
    }

    public static Object getDebuggerProxyObject(Object obj) {
        if (obj == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.obj);

        IDebugView debugView = DEBUG_VIEW_MAP.computeIfAbsent(obj, Debugger::getDebugView);
        return debugView.getProxyObject();
    }
}
