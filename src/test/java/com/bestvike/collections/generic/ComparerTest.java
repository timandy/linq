package com.bestvike.collections.generic;

import com.bestvike.CultureInfo;
import com.bestvike.IComparison;
import com.bestvike.TestCase;
import com.bestvike.linq.exception.ArgumentException;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by 许崇雷 on 2019-06-20.
 */
class ComparerTest extends TestCase {
    static {
        CultureInfo.setCurrent(Locale.CHINA);
        assertSame(Locale.CHINA, CultureInfo.getCurrent());
        assertEquals(Locale.ROOT, CultureInfo.getInvariant());
    }

    @Test
    void testInvariant() {
        Comparator<String> stringComparator = Comparer.DefaultInvariant();
        assertTrue(stringComparator.compare("编辑", "测试") > 0);
    }

    @Test
    void testDefault() {
        Comparator<String> stringComparator = Comparer.Default();
        assertTrue(stringComparator.compare("编辑", "测试") < 0);
    }

    @Test
    void testCreateWithCollator() {
        assertThrows(ArgumentNullException.class, () -> Comparer.create((Collator) null));

        Comparator<String> stringComparator = Comparer.create(Collator.getInstance(Locale.CHINA));
        assertTrue(stringComparator.compare("编辑", "测试") < 0);
    }

    @Test
    void testCreateWithIComparison() {
        assertThrows(ArgumentNullException.class, () -> Comparer.create((IComparison<Object>) null));

        Comparator<Object> objectComparator = Comparer.create((x, y) -> -1);
        assertTrue(objectComparator.compare(1, 2) < 0);
        assertTrue(objectComparator.compare(2, 1) < 0);
    }

    @Test
    void testNotImplementComparable() {
        assertThrows(ArgumentException.class, () -> Comparer.Default().compare(depts[0], depts[1]));
    }

    @Test
    void testFloat() {
        Comparator<Float> comparator = Comparer.Float();
        assertEquals(0, comparator.compare(null, null));
        assertEquals(1, comparator.compare(Float.NaN, null));
        assertEquals(-1, comparator.compare(null, Float.NaN));
        //
        assertEquals(1, comparator.compare(1f, 0f));
        assertEquals(0, comparator.compare(1f, 1f));
        assertEquals(-1, comparator.compare(0f, 1f));
        //
        assertEquals(1, comparator.compare(0f, Float.NaN));
        assertEquals(0, comparator.compare(Float.NaN, Float.NaN));
        assertEquals(-1, comparator.compare(Float.NaN, 0f));
    }

    @Test
    void testDouble() {
        Comparator<Double> comparator = Comparer.Double();
        assertEquals(0, comparator.compare(null, null));
        assertEquals(1, comparator.compare(Double.NaN, null));
        assertEquals(-1, comparator.compare(null, Double.NaN));
        //
        assertEquals(1, comparator.compare(1d, 0d));
        assertEquals(0, comparator.compare(1d, 1d));
        assertEquals(-1, comparator.compare(0d, 1d));
        //
        assertEquals(1, comparator.compare(0d, Double.NaN));
        assertEquals(0, comparator.compare(Double.NaN, Double.NaN));
        assertEquals(-1, comparator.compare(Double.NaN, 0d));
    }
}
