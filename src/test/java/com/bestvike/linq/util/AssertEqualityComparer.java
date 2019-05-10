package com.bestvike.linq.util;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-05-10.
 */
public final class AssertEqualityComparer<T> implements IEqualityComparer<T> {
    private static final IEqualityComparer<Object> innerComparer = new AssertEqualityComparer<>();

    @Override
    public boolean equals(T x, T y) {
        if (x instanceof IEnumerable && y instanceof IEnumerable)
            return ((IEnumerable) x).sequenceEqual((IEnumerable) y, innerComparer);
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(T obj) {
        if (obj instanceof IEnumerable) {
            final int prime = 31;
            int result = 1;
            IEnumerable enumerable = (IEnumerable) obj;
            for (Object item : enumerable)
                result = prime * result + innerComparer.hashCode(item);
            return result;
        }
        return obj == null ? 0 : obj.hashCode();
    }
}
