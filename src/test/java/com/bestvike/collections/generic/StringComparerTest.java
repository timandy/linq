package com.bestvike.collections.generic;

import com.bestvike.CultureInfo;
import com.bestvike.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

/**
 * Created by 许崇雷 on 2019-04-30.
 */
public class StringComparerTest extends TestCase {
    @Before
    public void before() {
        CultureInfo.setCurrent(Locale.CHINA);
        assertEquals(Locale.ROOT, CultureInfo.getInvariant());
    }

    @Test
    public void testEquals() {
        String a = "abc123你好";
        String b = "AbC123你好";
        assertFalse(StringComparer.CurrentCulture.equals(a, b));
        assertTrue(StringComparer.CurrentCultureIgnoreCase.equals(a, b));
        assertFalse(StringComparer.InvariantCulture.equals(a, b));
        assertTrue(StringComparer.InvariantCultureIgnoreCase.equals(a, b));
        assertFalse(StringComparer.Ordinal.equals(a, b));
        assertTrue(StringComparer.OrdinalIgnoreCase.equals(a, b));
    }

    @Test
    public void testHashCode() {
        String a = "abc123你好";
        String b = "AbC123你好";
        assertNotEquals(StringComparer.CurrentCulture.hashCode(a), StringComparer.CurrentCulture.hashCode(b));
        assertEquals(StringComparer.CurrentCultureIgnoreCase.hashCode(a), StringComparer.CurrentCultureIgnoreCase.hashCode(b));
        assertNotEquals(StringComparer.InvariantCulture.hashCode(a), StringComparer.InvariantCulture.hashCode(b));
        assertEquals(StringComparer.InvariantCultureIgnoreCase.hashCode(a), StringComparer.InvariantCultureIgnoreCase.hashCode(b));
        assertNotEquals(StringComparer.Ordinal.hashCode(a), StringComparer.Ordinal.hashCode(b));
        assertEquals(StringComparer.OrdinalIgnoreCase.hashCode(a), StringComparer.OrdinalIgnoreCase.hashCode(b));
    }

    @Test
    public void testCompare() {
        String a = "abc123你好";
        String b = "AbC123你好";
        assertTrue(StringComparer.CurrentCulture.compare(a, b) < 0);
        assertTrue(StringComparer.CurrentCultureIgnoreCase.compare(a, b) == 0);
        assertTrue(StringComparer.InvariantCulture.compare(a, b) < 0);
        assertTrue(StringComparer.InvariantCultureIgnoreCase.compare(a, b) == 0);
        assertTrue(StringComparer.Ordinal.compare(a, b) > 0);
        assertTrue(StringComparer.OrdinalIgnoreCase.compare(a, b) == 0);

        a = "李四 123 abc";
        b = "张三 123 ABC";
        assertTrue(StringComparer.CurrentCulture.compare(a, b) < 0);
        assertTrue(StringComparer.CurrentCultureIgnoreCase.compare(a, b) < 0);
        assertTrue(StringComparer.InvariantCulture.compare(a, b) > 0);
        assertTrue(StringComparer.InvariantCultureIgnoreCase.compare(a, b) > 0);
        assertTrue(StringComparer.Ordinal.compare(a, b) > 0);
        assertTrue(StringComparer.OrdinalIgnoreCase.compare(a, b) > 0);
    }
}
