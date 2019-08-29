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
    {
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
        assertThrows(ArgumentNullException.class, () -> Comparer.create((IComparison) null));

        Comparator<Object> objectComparator = Comparer.create((x, y) -> -1);
        assertTrue(objectComparator.compare(1, 2) < 0);
        assertTrue(objectComparator.compare(2, 1) < 0);
    }

    @Test
    void testNotImplementComparable() {
        assertThrows(ArgumentException.class, () -> Comparer.Default().compare(depts[0], depts[1]));
    }
}
