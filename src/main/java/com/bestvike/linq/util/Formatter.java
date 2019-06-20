package com.bestvike.linq.util;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ThrowHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 许崇雷 on 2019-06-20.
 */
@SuppressWarnings("Duplicates")
public final class Formatter {
    public static final Formatter DEFAULT = new Formatter();
    private static final String JDK_PREFIX = "java";
    private String nullString = "null";
    private String stringQuotes = "'";
    private boolean decimalWithScale = true;
    private int decimalScale = 6;
    private int decimalRounding = BigDecimal.ROUND_HALF_EVEN;
    private boolean objectWithType = true;
    private String objectPrefix = "{";
    private String objectSuffix = "}";
    private String objectEmpty = "{}";
    private String objectFieldSeparator = ", ";
    private String objectFieldValueSeparator = "=";
    private boolean arrayWithType = false;
    private String arrayPrefix = "[";
    private String arraySuffix = "]";
    private String arrayEmpty = "[]";
    private String arrayValueSeparator = ", ";
    private boolean mapWithType = true;
    private String mapPrefix = "{";
    private String mapSuffix = "}";
    private String mapEmpty = "{}";
    private String mapEntrySeparator = ", ";
    private String mapKeyValueSeparator = "=";

    //region properties

    public String getNullString() {
        return this.nullString;
    }

    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    public String getStringQuotes() {
        return this.stringQuotes;
    }

    public void setStringQuotes(String stringQuotes) {
        this.stringQuotes = stringQuotes;
    }

    public boolean isDecimalWithScale() {
        return this.decimalWithScale;
    }

    public void setDecimalWithScale(boolean decimalWithScale) {
        this.decimalWithScale = decimalWithScale;
    }

    public int getDecimalScale() {
        return this.decimalScale;
    }

    public void setDecimalScale(int decimalScale) {
        this.decimalScale = decimalScale;
    }

    public int getDecimalRounding() {
        return this.decimalRounding;
    }

    public void setDecimalRounding(int decimalRounding) {
        this.decimalRounding = decimalRounding;
    }

    public boolean isObjectWithType() {
        return this.objectWithType;
    }

    public void setObjectWithType(boolean objectWithType) {
        this.objectWithType = objectWithType;
    }

    public String getObjectPrefix() {
        return this.objectPrefix;
    }

    public void setObjectPrefix(String objectPrefix) {
        this.objectPrefix = objectPrefix;
    }

    public String getObjectSuffix() {
        return this.objectSuffix;
    }

    public void setObjectSuffix(String objectSuffix) {
        this.objectSuffix = objectSuffix;
    }

    public String getObjectEmpty() {
        return this.objectEmpty;
    }

    public void setObjectEmpty(String objectEmpty) {
        this.objectEmpty = objectEmpty;
    }

    public String getObjectFieldSeparator() {
        return this.objectFieldSeparator;
    }

    public void setObjectFieldSeparator(String objectFieldSeparator) {
        this.objectFieldSeparator = objectFieldSeparator;
    }

    public String getObjectFieldValueSeparator() {
        return this.objectFieldValueSeparator;
    }

    public void setObjectFieldValueSeparator(String objectFieldValueSeparator) {
        this.objectFieldValueSeparator = objectFieldValueSeparator;
    }

    public boolean isArrayWithType() {
        return this.arrayWithType;
    }

    public void setArrayWithType(boolean arrayWithType) {
        this.arrayWithType = arrayWithType;
    }

    public String getArrayPrefix() {
        return this.arrayPrefix;
    }

    public void setArrayPrefix(String arrayPrefix) {
        this.arrayPrefix = arrayPrefix;
    }

    public String getArraySuffix() {
        return this.arraySuffix;
    }

    public void setArraySuffix(String arraySuffix) {
        this.arraySuffix = arraySuffix;
    }

    public String getArrayEmpty() {
        return this.arrayEmpty;
    }

    public void setArrayEmpty(String arrayEmpty) {
        this.arrayEmpty = arrayEmpty;
    }

    public String getArrayValueSeparator() {
        return this.arrayValueSeparator;
    }

    public void setArrayValueSeparator(String arrayValueSeparator) {
        this.arrayValueSeparator = arrayValueSeparator;
    }

    public boolean isMapWithType() {
        return this.mapWithType;
    }

    public void setMapWithType(boolean mapWithType) {
        this.mapWithType = mapWithType;
    }

    public String getMapPrefix() {
        return this.mapPrefix;
    }

    public void setMapPrefix(String mapPrefix) {
        this.mapPrefix = mapPrefix;
    }

    public String getMapSuffix() {
        return this.mapSuffix;
    }

    public void setMapSuffix(String mapSuffix) {
        this.mapSuffix = mapSuffix;
    }

    public String getMapEmpty() {
        return this.mapEmpty;
    }

    public void setMapEmpty(String mapEmpty) {
        this.mapEmpty = mapEmpty;
    }

    public String getMapEntrySeparator() {
        return this.mapEntrySeparator;
    }

    public void setMapEntrySeparator(String mapEntrySeparator) {
        this.mapEntrySeparator = mapEntrySeparator;
    }

    public String getMapKeyValueSeparator() {
        return this.mapKeyValueSeparator;
    }

    public void setMapKeyValueSeparator(String mapKeyValueSeparator) {
        this.mapKeyValueSeparator = mapKeyValueSeparator;
    }

    //endregion

    public String format(Object obj) {
        StringBuilder sb = new StringBuilder();
        this.format(obj, sb);
        return sb.toString();
    }

    private void format(Object obj, StringBuilder sb) {
        if (obj == null) {
            sb.append(this.nullString);
            return;
        }
        if (obj instanceof String) {
            sb.append(this.stringQuotes).append(obj).append(this.stringQuotes);
            return;
        }
        if (obj instanceof BigDecimal) {
            sb.append(this.decimalWithScale ? ((BigDecimal) obj).setScale(this.decimalScale, this.decimalRounding) : (BigDecimal) obj);
            return;
        }
        if (obj instanceof boolean[]) {
            this.format((boolean[]) obj, sb);
            return;
        }
        if (obj instanceof byte[]) {
            this.format((byte[]) obj, sb);
            return;
        }
        if (obj instanceof short[]) {
            this.format((short[]) obj, sb);
            return;
        }
        if (obj instanceof int[]) {
            this.format((int[]) obj, sb);
            return;
        }
        if (obj instanceof long[]) {
            this.format((long[]) obj, sb);
            return;
        }
        if (obj instanceof char[]) {
            this.format((char[]) obj, sb);
            return;
        }
        if (obj instanceof float[]) {
            this.format((float[]) obj, sb);
            return;
        }
        if (obj instanceof double[]) {
            this.format((double[]) obj, sb);
            return;
        }
        if (obj instanceof Object[]) {
            this.format((Object[]) obj, sb);
            return;
        }
        if (obj instanceof IEnumerable) {
            this.format((IEnumerable<?>) obj, sb);
            return;
        }
        if (obj instanceof Iterable) {
            this.format((Iterable<?>) obj, sb);
            return;
        }
        if (obj instanceof Map) {
            this.format((Map<?, ?>) obj, sb);
            return;
        }
        Class<?> clazz = obj.getClass();
        if (clazz.getName().startsWith(JDK_PREFIX)) {
            sb.append(obj);
            return;
        }
        if (this.objectWithType)
            sb.append(clazz.getSimpleName());
        Field[] fields = ReflectionUtils.getFields(clazz);
        if (fields.length <= 0) {
            sb.append(this.objectEmpty);
            return;
        }
        sb.append(this.objectPrefix);
        try {
            Field field = fields[0];
            sb.append(field.getName()).append(this.objectFieldValueSeparator);
            this.format(field.get(obj), sb);
            for (int i = 1; i < fields.length; i++) {
                field = fields[i];
                sb.append(this.objectFieldSeparator).append(field.getName()).append(this.objectFieldValueSeparator);
                this.format(field.get(obj), sb);
            }
        } catch (IllegalAccessException e) {
            ThrowHelper.throwRuntimeException(e);
        }
        sb.append(this.objectSuffix);
    }

    private void format(boolean[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(this.arrayValueSeparator).append(obj[i]);
        sb.append(this.arraySuffix);
    }

    private void format(byte[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(this.arrayValueSeparator).append(obj[i]);
        sb.append(this.arraySuffix);
    }

    private void format(short[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(this.arrayValueSeparator).append(obj[i]);
        sb.append(this.arraySuffix);
    }

    private void format(int[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(this.arrayValueSeparator).append(obj[i]);
        sb.append(this.arraySuffix);
    }

    private void format(long[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(this.arrayValueSeparator).append(obj[i]);
        sb.append(this.arraySuffix);
    }

    private void format(char[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(this.arrayValueSeparator).append(obj[i]);
        sb.append(this.arraySuffix);
    }

    private void format(float[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(this.arrayValueSeparator).append(obj[i]);
        sb.append(this.arraySuffix);
    }

    private void format(double[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix).append(obj[0]);
        for (int i = 1; i < obj.length; i++)
            sb.append(this.arrayValueSeparator).append(obj[i]);
        sb.append(this.arraySuffix);
    }

    private <T> void format(T[] obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.length <= 0) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix);
        this.format(obj[0], sb);
        for (int i = 1; i < obj.length; i++) {
            sb.append(this.arrayValueSeparator);
            this.format(obj[i], sb);
        }
        sb.append(this.arraySuffix);
    }

    private <T> void format(IEnumerable<T> obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        try (IEnumerator<T> it = obj.enumerator()) {
            if (!it.moveNext()) {
                sb.append(this.arrayEmpty);
                return;
            }
            sb.append(this.arrayPrefix);
            this.format(it.current(), sb);
            while (it.moveNext()) {
                sb.append(this.arrayValueSeparator);
                this.format(it.current(), sb);
            }
            sb.append(this.arraySuffix);
        }
    }

    private <T> void format(Iterable<T> obj, StringBuilder sb) {
        if (this.arrayWithType)
            sb.append(obj.getClass().getSimpleName());
        Iterator<T> it = obj.iterator();
        if (!it.hasNext()) {
            sb.append(this.arrayEmpty);
            return;
        }
        sb.append(this.arrayPrefix);
        this.format(it.next(), sb);
        while (it.hasNext()) {
            sb.append(this.arrayValueSeparator);
            this.format(it.next(), sb);
        }
        sb.append(this.arraySuffix);
    }

    private <K, V> void format(Map<K, V> obj, StringBuilder sb) {
        if (this.mapWithType)
            sb.append(obj.getClass().getSimpleName());
        if (obj.isEmpty()) {
            sb.append(this.mapEmpty);
            return;
        }
        TreeMap<K, V> treeMap = new TreeMap<>(Comparer.Default());
        treeMap.putAll(obj);
        Iterator<Map.Entry<K, V>> it = treeMap.entrySet().iterator();
        sb.append(this.mapPrefix);
        Map.Entry<K, V> entry = it.next();
        this.format(entry.getKey(), sb);
        sb.append(this.mapKeyValueSeparator);
        this.format(entry.getValue(), sb);
        while (it.hasNext()) {
            sb.append(this.mapEntrySeparator);
            entry = it.next();
            this.format(entry.getKey(), sb);
            sb.append(this.mapKeyValueSeparator);
            this.format(entry.getValue(), sb);
        }
        sb.append(this.mapSuffix);
    }
}
