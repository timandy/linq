package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.Formatter;
import com.bestvike.linq.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 许崇雷 on 2018-05-29.
 */
public final class Values {
    private static final int DECIMAL_SCALE = 6;
    private static final int DECIMAL_ROUNDING = BigDecimal.ROUND_HALF_EVEN;
    private static final int HASH_PRIME = 31;
    private static final int HASH_NULL = 0;
    private static final int HASH_EMPTY = 1;
    private static final int HASH_TRUE = 1231;
    private static final int HASH_FALSE = 1237;
    private static final String JDK_PREFIX = "java";

    private Values() {
    }

    public static boolean equals(Object x, Object y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        if (x instanceof BigDecimal && y instanceof BigDecimal)
            return ((BigDecimal) x).compareTo((BigDecimal) y) == 0;
        if (x instanceof boolean[])
            return equals((boolean[]) x, y);
        if (x instanceof byte[])
            return equals((byte[]) x, y);
        if (x instanceof short[])
            return equals((short[]) x, y);
        if (x instanceof int[])
            return equals((int[]) x, y);
        if (x instanceof long[])
            return equals((long[]) x, y);
        if (x instanceof char[])
            return equals((char[]) x, y);
        if (x instanceof float[])
            return equals((float[]) x, y);
        if (x instanceof double[])
            return equals((double[]) x, y);
        if (x instanceof Object[])
            return equals((Object[]) x, y);
        if (x instanceof IEnumerable) {
            if (x instanceof ICollection) {
                ICollection<?> collection = (ICollection<?>) x;
                return equals(collection, y, collection._getCount());
            } else if (x instanceof IIListProvider) {
                IIListProvider<?> listProvider = (IIListProvider<?>) x;
                return equals(listProvider, y, listProvider._getCount(true));
            }
            return equals((IEnumerable<?>) x, y, -1);
        }
        if (x instanceof Iterable) {
            if (x instanceof Collection) {
                Collection<?> collection = (Collection<?>) x;
                return equals(collection, y, collection.size());
            }
            return equals((Iterable<?>) x, y, -1);
        }
        if (x instanceof Map)
            return equals((Map<?, ?>) x, y);
        Class<?> clazz = x.getClass();
        if (clazz.getName().startsWith(JDK_PREFIX))
            return x.equals(y);
        if (clazz != y.getClass())
            return false;
        Field[] fields = ReflectionUtils.getFields(clazz);
        try {
            for (Field field : fields) {
                if (!equals(field.get(x), field.get(y)))
                    return false;
            }
        } catch (IllegalAccessException e) {
            ThrowHelper.throwRuntimeException(e);
        }
        return true;
    }

    private static boolean equals(boolean[] x, Object y) {
        if (y instanceof boolean[]) {
            boolean[] arrY = (boolean[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != arrY[i])
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Boolean) x[i]).equals(arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (Boolean curX : x) {
                    if (!(itY.moveNext() && curX.equals(itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (Boolean curX : x) {
                if (!(itY.hasNext() && curX.equals(itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static boolean equals(byte[] x, Object y) {
        if (y instanceof byte[]) {
            byte[] arrY = (byte[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != arrY[i])
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Byte) x[i]).equals(arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (Byte curX : x) {
                    if (!(itY.moveNext() && curX.equals(itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (Byte curX : x) {
                if (!(itY.hasNext() && curX.equals(itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static boolean equals(short[] x, Object y) {
        if (y instanceof short[]) {
            short[] arrY = (short[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != arrY[i])
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Short) x[i]).equals(arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (Short curX : x) {
                    if (!(itY.moveNext() && curX.equals(itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (Short curX : x) {
                if (!(itY.hasNext() && curX.equals(itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static boolean equals(int[] x, Object y) {
        if (y instanceof int[]) {
            int[] arrY = (int[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != arrY[i])
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Integer) x[i]).equals(arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (Integer curX : x) {
                    if (!(itY.moveNext() && curX.equals(itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (Integer curX : x) {
                if (!(itY.hasNext() && curX.equals(itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static boolean equals(long[] x, Object y) {
        if (y instanceof long[]) {
            long[] arrY = (long[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != arrY[i])
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Long) x[i]).equals(arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (Long curX : x) {
                    if (!(itY.moveNext() && curX.equals(itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (Long curX : x) {
                if (!(itY.hasNext() && curX.equals(itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static boolean equals(char[] x, Object y) {
        if (y instanceof char[]) {
            char[] arrY = (char[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != arrY[i])
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Character) x[i]).equals(arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (Character curX : x) {
                    if (!(itY.moveNext() && curX.equals(itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (Character curX : x) {
                if (!(itY.hasNext() && curX.equals(itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static boolean equals(float[] x, Object y) {
        if (y instanceof float[]) {
            float[] arrY = (float[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != arrY[i])
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Float) x[i]).equals(arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (Float curX : x) {
                    if (!(itY.moveNext() && curX.equals(itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (Float curX : x) {
                if (!(itY.hasNext() && curX.equals(itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static boolean equals(double[] x, Object y) {
        if (y instanceof double[]) {
            double[] arrY = (double[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != arrY[i])
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Double) x[i]).equals(arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (Double curX : x) {
                    if (!(itY.moveNext() && curX.equals(itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (Double curX : x) {
                if (!(itY.hasNext() && curX.equals(itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static <T> boolean equals(T[] x, Object y) {
        if (y instanceof boolean[]) {
            boolean[] arrY = (boolean[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Boolean) arrY[i]).equals(x[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof byte[]) {
            byte[] arrY = (byte[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Byte) arrY[i]).equals(x[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof short[]) {
            short[] arrY = (short[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Short) arrY[i]).equals(x[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof int[]) {
            int[] arrY = (int[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Integer) arrY[i]).equals(x[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof long[]) {
            long[] arrY = (long[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Long) arrY[i]).equals(x[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof char[]) {
            char[] arrY = (char[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Character) arrY[i]).equals(x[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof float[]) {
            float[] arrY = (float[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Float) arrY[i]).equals(x[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof double[]) {
            double[] arrY = (double[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!((Double) arrY[i]).equals(x[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (x.length != arrY.length)
                return false;
            for (int i = 0; i < x.length; i++) {
                if (!equals(x[i], arrY[i]))
                    return false;
            }
            return true;
        }
        if (y instanceof IEnumerable) {
            if (y instanceof ICollection) {
                ICollection listY = (ICollection) y;
                if (x.length != listY._getCount())
                    return false;
            } else if (y instanceof IIListProvider) {
                IIListProvider listY = (IIListProvider) y;
                int lenY = listY._getCount(true);
                if (lenY != -1 && x.length != lenY)
                    return false;
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (T curX : x) {
                    if (!(itY.moveNext() && equals(curX, itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (y instanceof Collection) {
                Collection listY = (Collection) y;
                if (x.length != listY.size())
                    return false;
            }
            Iterator itY = ((Iterable) y).iterator();
            for (T curX : x) {
                if (!(itY.hasNext() && equals(curX, itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static <T> boolean equals(IEnumerable<T> x, Object y, int lenX) {
        if (y instanceof boolean[]) {
            boolean[] arrY = (boolean[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Boolean curY : arrY) {
                    if (!(itX.moveNext() && curY.equals(itX.current())))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof byte[]) {
            byte[] arrY = (byte[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Byte curY : arrY) {
                    if (!(itX.moveNext() && curY.equals(itX.current())))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof short[]) {
            short[] arrY = (short[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Short curY : arrY) {
                    if (!(itX.moveNext() && curY.equals(itX.current())))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof int[]) {
            int[] arrY = (int[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Integer curY : arrY) {
                    if (!(itX.moveNext() && curY.equals(itX.current())))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof long[]) {
            long[] arrY = (long[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Long curY : arrY) {
                    if (!(itX.moveNext() && curY.equals(itX.current())))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof char[]) {
            char[] arrY = (char[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Character curY : arrY) {
                    if (!(itX.moveNext() && curY.equals(itX.current())))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof float[]) {
            float[] arrY = (float[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Float curY : arrY) {
                    if (!(itX.moveNext() && curY.equals(itX.current())))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof double[]) {
            double[] arrY = (double[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Double curY : arrY) {
                    if (!(itX.moveNext() && curY.equals(itX.current())))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            try (IEnumerator<T> itX = x.enumerator()) {
                for (Object curY : arrY) {
                    if (!(itX.moveNext() && equals(itX.current(), curY)))
                        return false;
                }
                return !itX.moveNext();
            }
        }
        if (y instanceof IEnumerable) {
            if (lenX != -1) {
                if (y instanceof ICollection) {
                    ICollection listY = (ICollection) y;
                    if (lenX != listY._getCount())
                        return false;
                } else if (y instanceof IIListProvider) {
                    IIListProvider listY = (IIListProvider) y;
                    int lenY = listY._getCount(true);
                    if (lenY != -1 && lenX != lenY)
                        return false;
                }
            }
            try (IEnumerator<T> itX = x.enumerator();
                 IEnumerator itY = ((IEnumerable) y).enumerator()) {
                while (itX.moveNext()) {
                    if (!(itY.moveNext() && equals(itX.current(), itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (lenX != -1) {
                if (y instanceof Collection) {
                    Collection listY = (Collection) y;
                    if (lenX != listY.size())
                        return false;
                }
            }
            try (IEnumerator<T> itX = x.enumerator()) {
                Iterator itY = ((Iterable) y).iterator();
                while (itX.moveNext()) {
                    if (!(itY.hasNext() && equals(itX.current(), itY.next())))
                        return false;
                }
                return !itY.hasNext();
            }
        }
        return false;
    }

    private static <T> boolean equals(Iterable<T> x, Object y, int lenX) {
        if (y instanceof boolean[]) {
            boolean[] arrY = (boolean[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Boolean curY : arrY) {
                if (!(itX.hasNext() && curY.equals(itX.next())))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof byte[]) {
            byte[] arrY = (byte[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Byte curY : arrY) {
                if (!(itX.hasNext() && curY.equals(itX.next())))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof short[]) {
            short[] arrY = (short[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Short curY : arrY) {
                if (!(itX.hasNext() && curY.equals(itX.next())))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof int[]) {
            int[] arrY = (int[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Integer curY : arrY) {
                if (!(itX.hasNext() && curY.equals(itX.next())))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof long[]) {
            long[] arrY = (long[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Long curY : arrY) {
                if (!(itX.hasNext() && curY.equals(itX.next())))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof char[]) {
            char[] arrY = (char[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Character curY : arrY) {
                if (!(itX.hasNext() && curY.equals(itX.next())))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof float[]) {
            float[] arrY = (float[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Float curY : arrY) {
                if (!(itX.hasNext() && curY.equals(itX.next())))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof double[]) {
            double[] arrY = (double[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Double curY : arrY) {
                if (!(itX.hasNext() && curY.equals(itX.next())))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof Object[]) {
            Object[] arrY = (Object[]) y;
            if (lenX != -1 && lenX != arrY.length)
                return false;
            Iterator<T> itX = x.iterator();
            for (Object curY : arrY) {
                if (!(itX.hasNext() && equals(itX.next(), curY)))
                    return false;
            }
            return !itX.hasNext();
        }
        if (y instanceof IEnumerable) {
            if (lenX != -1) {
                if (y instanceof ICollection) {
                    ICollection listY = (ICollection) y;
                    if (lenX != listY._getCount())
                        return false;
                } else if (y instanceof IIListProvider) {
                    IIListProvider listY = (IIListProvider) y;
                    int lenY = listY._getCount(true);
                    if (lenY != -1 && lenX != lenY)
                        return false;
                }
            }
            try (IEnumerator itY = ((IEnumerable) y).enumerator()) {
                for (T curX : x) {
                    if (!(itY.moveNext() && equals(curX, itY.current())))
                        return false;
                }
                return !itY.moveNext();
            }
        }
        if (y instanceof Iterable) {
            if (lenX != -1) {
                if (y instanceof Collection) {
                    Collection listY = (Collection) y;
                    if (lenX != listY.size())
                        return false;
                }
            }
            Iterator itY = ((Iterable) y).iterator();
            for (T curX : x) {
                if (!(itY.hasNext() && equals(curX, itY.next())))
                    return false;
            }
            return !itY.hasNext();
        }
        return false;
    }

    private static <K, V> boolean equals(Map<K, V> x, Object y) {
        if (y instanceof Map) {
            Map mapY = (Map) y;
            if (x.size() != mapY.size())
                return false;
            for (Map.Entry<K, V> entry : x.entrySet()) {
                if (!equals(entry.getValue(), mapY.get(entry.getKey())))
                    return false;
            }
            return true;
        }
        return false;
    }

    public static int hashCode(Object obj) {
        if (obj == null)
            return HASH_NULL;
        if (obj instanceof BigDecimal)
            return ((BigDecimal) obj).setScale(DECIMAL_SCALE, DECIMAL_ROUNDING).hashCode();
        if (obj instanceof boolean[])
            return hashCode((boolean[]) obj);
        if (obj instanceof byte[])
            return hashCode((byte[]) obj);
        if (obj instanceof short[])
            return hashCode((short[]) obj);
        if (obj instanceof int[])
            return hashCode((int[]) obj);
        if (obj instanceof long[])
            return hashCode((long[]) obj);
        if (obj instanceof char[])
            return hashCode((char[]) obj);
        if (obj instanceof float[])
            return hashCode((float[]) obj);
        if (obj instanceof double[])
            return hashCode((double[]) obj);
        if (obj instanceof Object[])
            return hashCode((Object[]) obj);
        if (obj instanceof IEnumerable)
            return hashCode((IEnumerable<?>) obj);
        if (obj instanceof Iterable)
            return hashCode((Iterable<?>) obj);
        if (obj instanceof Map)
            return hashCode((Map<?, ?>) obj);
        Class<?> clazz = obj.getClass();
        if (clazz.getName().startsWith(JDK_PREFIX))
            return obj.hashCode();
        int result = HASH_EMPTY;
        Field[] fields = ReflectionUtils.getFields(clazz);
        try {
            for (Field field : fields)
                result = HASH_PRIME * result + hashCode(field.get(obj));
        } catch (IllegalAccessException e) {
            ThrowHelper.throwRuntimeException(e);
        }
        return result;
    }

    private static int hashCode(boolean[] obj) {
        int result = HASH_EMPTY;
        for (boolean element : obj)
            result = HASH_PRIME * result + (element ? HASH_TRUE : HASH_FALSE);
        return result;
    }

    private static int hashCode(byte[] obj) {
        int result = HASH_EMPTY;
        for (byte element : obj)
            result = HASH_PRIME * result + element;
        return result;
    }

    private static int hashCode(short[] obj) {
        int result = HASH_EMPTY;
        for (short element : obj)
            result = HASH_PRIME * result + element;
        return result;
    }

    private static int hashCode(int[] obj) {
        int result = HASH_EMPTY;
        for (int element : obj)
            result = HASH_PRIME * result + element;
        return result;
    }

    private static int hashCode(long[] obj) {
        int result = HASH_EMPTY;
        for (long element : obj)
            result = HASH_PRIME * result + (int) (element ^ (element >>> 32));
        return result;
    }

    private static int hashCode(char[] obj) {
        int result = HASH_EMPTY;
        for (char element : obj)
            result = HASH_PRIME * result + element;
        return result;
    }

    private static int hashCode(float[] obj) {
        int result = HASH_EMPTY;
        for (float element : obj)
            result = HASH_PRIME * result + Float.floatToIntBits(element);
        return result;
    }

    private static int hashCode(double[] obj) {
        int result = HASH_EMPTY;
        for (double element : obj) {
            long bits = Double.doubleToLongBits(element);
            result = HASH_PRIME * result + (int) (bits ^ (bits >>> 32));
        }
        return result;
    }

    private static <T> int hashCode(T[] obj) {
        int result = HASH_EMPTY;
        for (T element : obj)
            result = HASH_PRIME * result + hashCode(element);
        return result;
    }

    private static <T> int hashCode(IEnumerable<T> obj) {
        int result = HASH_EMPTY;
        try (IEnumerator<T> enumerator = obj.enumerator()) {
            while (enumerator.moveNext())
                result = HASH_PRIME * result + hashCode(enumerator.current());
        }
        return result;
    }

    private static <T> int hashCode(Iterable<T> obj) {
        int result = HASH_EMPTY;
        for (T element : obj)
            result = HASH_PRIME * result + hashCode(element);
        return result;
    }

    private static <K, V> int hashCode(Map<K, V> obj) {
        int result = HASH_EMPTY;
        TreeMap<K, V> treeMap = new TreeMap<>(Comparer.Default());
        treeMap.putAll(obj);
        for (Map.Entry<K, V> entry : treeMap.entrySet()) {
            result = HASH_PRIME * result + hashCode(entry.getKey());
            result = HASH_PRIME * result + hashCode(entry.getValue());
        }
        return result;
    }

    public static String toString(Object obj) {
        return Formatter.DEFAULT.format(obj);
    }
}
