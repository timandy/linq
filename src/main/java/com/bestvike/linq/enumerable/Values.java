package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ThrowHelper;
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
@SuppressWarnings("Duplicates")
public final class Values {
    private static final int DECIMAL_SCALE = 6;
    private static final int DECIMAL_ROUNDING = BigDecimal.ROUND_HALF_EVEN;
    private static final int HASH_PRIME = 31;
    private static final int HASH_NULL = 0;
    private static final int HASH_EMPTY = 1;
    private static final int HASH_TRUE = 1231;
    private static final int HASH_FALSE = 1237;
    private static final String JDK_PREFIX = "java";
    private static final String STRING_NULL = "null";
    private static final String STRING_STRING_QUOT = "'";
    private static final boolean STRING_OBJECT_TYPE = true;
    private static final String STRING_OBJECT_PREFIX = "{";
    private static final String STRING_OBJECT_SUFFIX = "}";
    private static final String STRING_OBJECT_EMPTY = "{}";
    private static final String STRING_OBJECT_SEPARATOR = ", ";
    private static final String STRING_OBJECT_KEY_VALUE_SEPARATOR = "=";
    private static final boolean STRING_ARRAY_TYPE = false;
    private static final String STRING_ARRAY_PREFIX = "[";
    private static final String STRING_ARRAY_SUFFIX = "]";
    private static final String STRING_ARRAY_EMPTY = "[]";
    private static final String STRING_ARRAY_SEPARATOR = ", ";
    private static final boolean STRING_MAP_TYPE = true;
    private static final String STRING_MAP_PREFIX = "{";
    private static final String STRING_MAP_SUFFIX = "}";
    private static final String STRING_MAP_EMPTY = "{}";
    private static final String STRING_MAP_SEPARATOR = ", ";
    private static final String STRING_MAP_KEY_VALUE_SEPARATOR = "=";

    private Values() {
    }

    public static boolean equals(Object x, Object y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        Class<?> clazz = x.getClass();
        if (clazz != y.getClass())
            return false;
        if (x instanceof BigDecimal)
            return ((BigDecimal) x).compareTo((BigDecimal) y) == 0;
        if (x instanceof boolean[])
            return equals((boolean[]) x, (boolean[]) y);
        if (x instanceof byte[])
            return equals((byte[]) x, (byte[]) y);
        if (x instanceof short[])
            return equals((short[]) x, (short[]) y);
        if (x instanceof int[])
            return equals((int[]) x, (int[]) y);
        if (x instanceof long[])
            return equals((long[]) x, (long[]) y);
        if (x instanceof char[])
            return equals((char[]) x, (char[]) y);
        if (x instanceof float[])
            return equals((float[]) x, (float[]) y);
        if (x instanceof double[])
            return equals((double[]) x, (double[]) y);
        if (x instanceof Object[])
            return equals(((Object[]) x), ((Object[]) y));
        if (x instanceof ICollection)
            return equals((ICollection<?>) x, (ICollection<?>) y);
        if (x instanceof IIListProvider)
            return equals((IIListProvider<?>) x, (IIListProvider<?>) y);
        if (x instanceof IEnumerable)
            return equals((IEnumerable<?>) x, (IEnumerable<?>) y);
        if (x instanceof Collection)
            return equals((Collection<?>) x, (Collection<?>) y);
        if (x instanceof Iterable)
            return equals((Iterable<?>) x, (Iterable<?>) y);
        if (x instanceof Map)
            return equals((Map<?, ?>) x, (Map<?, ?>) y);
        if (clazz.getName().startsWith(JDK_PREFIX))
            return x.equals(y);
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

    private static boolean equals(boolean[] x, boolean[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (x[i] != y[i])
                return false;
        }
        return true;
    }

    private static boolean equals(byte[] x, byte[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (x[i] != y[i])
                return false;
        }
        return true;
    }

    private static boolean equals(short[] x, short[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (x[i] != y[i])
                return false;
        }
        return true;
    }

    private static boolean equals(int[] x, int[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (x[i] != y[i])
                return false;
        }
        return true;
    }

    private static boolean equals(long[] x, long[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (x[i] != y[i])
                return false;
        }
        return true;
    }

    private static boolean equals(char[] x, char[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (x[i] != y[i])
                return false;
        }
        return true;
    }

    private static boolean equals(float[] x, float[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (x[i] != y[i])
                return false;
        }
        return true;
    }

    private static boolean equals(double[] x, double[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (x[i] != y[i])
                return false;
        }
        return true;
    }

    private static <X, Y> boolean equals(X[] x, Y[] y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int length = x.length;
        if (length != y.length)
            return false;
        for (int i = 0; i < length; i++) {
            if (!equals(x[i], y[i]))
                return false;
        }
        return true;
    }

    private static <X, Y> boolean equals(ICollection<X> x, ICollection<Y> y) {
        if (x == y)
            return true;
        if (x == null || y == null || x._getCount() != y._getCount())
            return false;
        IEnumerator<X> itX = x.enumerator();
        IEnumerator<Y> itY = y.enumerator();
        while (itX.moveNext() && itY.moveNext()) {
            if (!equals(itX.current(), itY.current()))
                return false;
        }
        return !(itX.moveNext() || itY.moveNext());
    }

    private static <X, Y> boolean equals(IIListProvider<X> x, IIListProvider<Y> y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        int lenX = x._getCount(true);
        int lenY = y._getCount(true);
        if (lenX != -1 && lenY != -1 && lenX != lenY)
            return false;
        IEnumerator<X> itX = x.enumerator();
        IEnumerator<Y> itY = y.enumerator();
        while (itX.moveNext() && itY.moveNext()) {
            if (!equals(itX.current(), itY.current()))
                return false;
        }
        return !(itX.moveNext() || itY.moveNext());
    }

    private static <X, Y> boolean equals(IEnumerable<X> x, IEnumerable<Y> y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        IEnumerator<X> itX = x.enumerator();
        IEnumerator<Y> itY = y.enumerator();
        while (itX.moveNext() && itY.moveNext()) {
            if (!equals(itX.current(), itY.current()))
                return false;
        }
        return !(itX.moveNext() || itY.moveNext());
    }

    private static <X, Y> boolean equals(Collection<X> x, Collection<Y> y) {
        if (x == y)
            return true;
        if (x == null || y == null || x.size() != y.size())
            return false;
        Iterator<X> itX = x.iterator();
        Iterator<Y> itY = y.iterator();
        while (itX.hasNext() && itY.hasNext()) {
            if (!equals(itX.next(), itY.next()))
                return false;
        }
        return !(itX.hasNext() || itY.hasNext());
    }

    private static <X, Y> boolean equals(Iterable<X> x, Iterable<Y> y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        Iterator<X> itX = x.iterator();
        Iterator<Y> itY = y.iterator();
        while (itX.hasNext() && itY.hasNext()) {
            if (!equals(itX.next(), itY.next()))
                return false;
        }
        return !(itX.hasNext() || itY.hasNext());
    }

    private static <XK, XV, YK, YV> boolean equals(Map<XK, XV> x, Map<YK, YV> y) {
        if (x == y)
            return true;
        if (x == null || y == null || x.size() != y.size())
            return false;
        for (Map.Entry<XK, XV> entry : x.entrySet()) {
            if (!equals(entry.getValue(), y.get(entry.getKey())))
                return false;
        }
        return true;
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
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (boolean element : obj)
            result = HASH_PRIME * result + (element ? HASH_TRUE : HASH_FALSE);
        return result;
    }

    private static int hashCode(byte[] obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (byte element : obj)
            result = HASH_PRIME * result + element;
        return result;
    }

    private static int hashCode(short[] obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (short element : obj)
            result = HASH_PRIME * result + element;
        return result;
    }

    private static int hashCode(int[] obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (int element : obj)
            result = HASH_PRIME * result + element;
        return result;
    }

    private static int hashCode(long[] obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (long element : obj)
            result = HASH_PRIME * result + (int) (element ^ (element >>> 32));
        return result;
    }

    private static int hashCode(char[] obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (char element : obj)
            result = HASH_PRIME * result + element;
        return result;
    }

    private static int hashCode(float[] obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (float element : obj)
            result = HASH_PRIME * result + Float.floatToIntBits(element);
        return result;
    }

    private static int hashCode(double[] obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (double element : obj) {
            long bits = Double.doubleToLongBits(element);
            result = HASH_PRIME * result + (int) (bits ^ (bits >>> 32));
        }
        return result;
    }

    private static <T> int hashCode(T[] obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (T element : obj)
            result = HASH_PRIME * result + hashCode(element);
        return result;
    }

    private static <T> int hashCode(Iterable<T> obj) {
        if (obj == null)
            return HASH_NULL;
        int result = HASH_EMPTY;
        for (T element : obj)
            result = HASH_PRIME * result + hashCode(element);
        return result;
    }

    private static <K, V> int hashCode(Map<K, V> obj) {
        if (obj == null)
            return HASH_NULL;
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
        StringBuilder sb = new StringBuilder();
        toString(obj, sb);
        return sb.toString();
    }

    private static void toString(Object obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (obj instanceof String) {
            sb.append(STRING_STRING_QUOT).append(obj).append(STRING_STRING_QUOT);
            return;
        }
        if (obj instanceof BigDecimal) {
            sb.append(((BigDecimal) obj).setScale(DECIMAL_SCALE, DECIMAL_ROUNDING));
            return;
        }
        if (obj instanceof boolean[]) {
            toString((boolean[]) obj, sb);
            return;
        }
        if (obj instanceof byte[]) {
            toString((byte[]) obj, sb);
            return;
        }
        if (obj instanceof short[]) {
            toString((short[]) obj, sb);
            return;
        }
        if (obj instanceof int[]) {
            toString((int[]) obj, sb);
            return;
        }
        if (obj instanceof long[]) {
            toString((long[]) obj, sb);
            return;
        }
        if (obj instanceof char[]) {
            toString((char[]) obj, sb);
            return;
        }
        if (obj instanceof float[]) {
            toString((float[]) obj, sb);
            return;
        }
        if (obj instanceof double[]) {
            toString((double[]) obj, sb);
            return;
        }
        if (obj instanceof Object[]) {
            toString((Object[]) obj, sb);
            return;
        }
        if (obj instanceof Iterable) {
            toString((Iterable<?>) obj, sb);
            return;
        }
        if (obj instanceof Map) {
            toString((Map<?, ?>) obj, sb);
            return;
        }
        Class<?> clazz = obj.getClass();
        if (clazz.getName().startsWith(JDK_PREFIX)) {
            sb.append(obj);
            return;
        }
        if (STRING_OBJECT_TYPE)
            sb.append(clazz.getSimpleName());
        Field[] fields = ReflectionUtils.getFields(clazz);
        if (fields.length <= 0) {
            sb.append(STRING_OBJECT_EMPTY);
            return;
        }
        sb.append(STRING_OBJECT_PREFIX);
        try {
            Field field = fields[0];
            sb.append(field.getName()).append(STRING_OBJECT_KEY_VALUE_SEPARATOR);
            toString(field.get(obj), sb);
            for (int i = 1; i < fields.length; i++) {
                field = fields[i];
                sb.append(STRING_OBJECT_SEPARATOR).append(field.getName()).append(STRING_OBJECT_KEY_VALUE_SEPARATOR);
                toString(field.get(obj), sb);
            }
        } catch (IllegalAccessException e) {
            ThrowHelper.throwRuntimeException(e);
        }
        sb.append(STRING_OBJECT_SUFFIX);
    }

    private static void toString(boolean[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(STRING_ARRAY_SEPARATOR).append(obj[i]);
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static void toString(byte[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(STRING_ARRAY_SEPARATOR).append(obj[i]);
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static void toString(short[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(STRING_ARRAY_SEPARATOR).append(obj[i]);
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static void toString(int[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(STRING_ARRAY_SEPARATOR).append(obj[i]);
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static void toString(long[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(STRING_ARRAY_SEPARATOR).append(obj[i]);
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static void toString(char[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(STRING_ARRAY_SEPARATOR).append(obj[i]);
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static void toString(float[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(STRING_ARRAY_SEPARATOR).append(obj[i]);
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static void toString(double[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(STRING_ARRAY_SEPARATOR).append(obj[i]);
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static <T> void toString(T[] obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX);
        toString(obj[0], sb);
        for (int i = 1; i < obj.length; i++) {
            sb.append(STRING_ARRAY_SEPARATOR);
            toString(obj[i], sb);
        }
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static <T> void toString(Iterable<T> obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_ARRAY_TYPE)
            sb.append(obj.getClass().getSimpleName());
        Iterator<T> it = obj.iterator();
        if (!it.hasNext()) {
            sb.append(STRING_ARRAY_EMPTY);
            return;
        }
        sb.append(STRING_ARRAY_PREFIX);
        toString(it.next(), sb);
        while (it.hasNext()) {
            sb.append(STRING_ARRAY_SEPARATOR);
            toString(it.next(), sb);
        }
        sb.append(STRING_ARRAY_SUFFIX);
    }

    private static <K, V> void toString(Map<K, V> obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(STRING_NULL);
            return;
        }
        if (STRING_MAP_TYPE)
            sb.append(obj.getClass().getSimpleName());
        if (obj.isEmpty()) {
            sb.append(STRING_MAP_EMPTY);
            return;
        }
        TreeMap<K, V> treeMap = new TreeMap<>(Comparer.Default());
        treeMap.putAll(obj);
        Iterator<Map.Entry<K, V>> it = treeMap.entrySet().iterator();
        sb.append(STRING_MAP_PREFIX);
        Map.Entry<K, V> entry = it.next();
        toString(entry.getKey(), sb);
        sb.append(STRING_MAP_KEY_VALUE_SEPARATOR);
        toString(entry.getValue(), sb);
        while (it.hasNext()) {
            sb.append(STRING_MAP_SEPARATOR);
            entry = it.next();
            toString(entry.getKey(), sb);
            sb.append(STRING_MAP_KEY_VALUE_SEPARATOR);
            toString(entry.getValue(), sb);
        }
        sb.append(STRING_MAP_SUFFIX);
    }
}
