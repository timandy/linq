package com.bestvike.linq.debug;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
public final class DebugView {
    private static final Map<Iterable<?>, IDebugView> DEBUG_VIEW_MAP = new ConcurrentHashMap<>();

    private DebugView() {
    }

    private static IDebugView getDebugView(Iterable<?> iterable) {
        if (iterable == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.iterable);

        return DEBUG_VIEW_MAP.computeIfAbsent(iterable, x -> {
            if (x instanceof ILookup)
                return new LookupDebugView<>((ILookup<?, ?>) x);
            if (x instanceof IGrouping)
                return new GroupingDebugView<>((IGrouping<?, ?>) x);
            if (x instanceof IEnumerable)
                return new EnumerableDebugView<>((IEnumerable<?>) x);
            return new IterableDebugView<>(x);
        });
    }

    public static String getDebuggerDisplay(Iterable<?> iterable) {
        return getDebugView(iterable).getDebuggerDisplay();
    }

    public static Object getDebuggerTypeProxy(Iterable<?> iterable) {
        return getDebugView(iterable).getDebuggerTypeProxy();
    }
}
