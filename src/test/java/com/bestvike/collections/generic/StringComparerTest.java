package com.bestvike.collections.generic;

import com.bestvike.CultureInfo;
import com.bestvike.TestCase;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Before;
import org.junit.Test;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by 许崇雷 on 2019-04-30.
 */
public class StringComparerTest extends TestCase {
    @Before
    public void before() {
        CultureInfo.setCurrent(Locale.CHINA);
        assertSame(Locale.CHINA, CultureInfo.getCurrent());
        assertEquals(Locale.ROOT, CultureInfo.getInvariant());
    }

    @Test
    public void testCreate() {
        StringComparer caseSensitiveLocalComparer = StringComparer.create(Collator.getInstance(Locale.ROOT), false);
        assertTrue(caseSensitiveLocalComparer.equals(null, null));
        assertFalse(caseSensitiveLocalComparer.equals(null, "a"));
        assertFalse(caseSensitiveLocalComparer.equals("a", null));
        assertFalse(caseSensitiveLocalComparer.equals("a", "A"));
        assertEquals(0, caseSensitiveLocalComparer.hashCode(null));
        assertNotEquals(caseSensitiveLocalComparer.hashCode("a"), caseSensitiveLocalComparer.hashCode("A"));
        assertTrue(caseSensitiveLocalComparer.compare(null, null) == 0);
        assertTrue(caseSensitiveLocalComparer.compare(null, "a") < 0);
        assertTrue(caseSensitiveLocalComparer.compare("a", null) > 0);
        assertTrue(caseSensitiveLocalComparer.compare("编辑", "测试") > 0);

        assertThrows(ArgumentNullException.class, () -> StringComparer.create(null, false));
        StringComparer ignoreCaseLocalComparer = StringComparer.create(Collator.getInstance(Locale.CHINA), true);
        assertTrue(ignoreCaseLocalComparer.equals(null, null));
        assertFalse(ignoreCaseLocalComparer.equals(null, "a"));
        assertFalse(ignoreCaseLocalComparer.equals("a", null));
        assertTrue(ignoreCaseLocalComparer.equals("a", "A"));
        assertEquals(0, ignoreCaseLocalComparer.hashCode(null));
        assertEquals(ignoreCaseLocalComparer.hashCode("a"), ignoreCaseLocalComparer.hashCode("A"));
        assertTrue(ignoreCaseLocalComparer.compare(null, null) == 0);
        assertTrue(ignoreCaseLocalComparer.compare(null, "a") < 0);
        assertTrue(ignoreCaseLocalComparer.compare("a", null) > 0);
        assertTrue(ignoreCaseLocalComparer.compare("编辑", "测试") < 0);

    }

    @Test
    public void testOrdinal() {
        StringComparer caseSensitiveLocalComparer = StringComparer.Ordinal;
        assertTrue(caseSensitiveLocalComparer.equals(null, null));
        assertFalse(caseSensitiveLocalComparer.equals(null, "a"));
        assertFalse(caseSensitiveLocalComparer.equals("a", null));
        assertFalse(caseSensitiveLocalComparer.equals("a", "A"));
        assertEquals(0, caseSensitiveLocalComparer.hashCode(null));
        assertNotEquals(caseSensitiveLocalComparer.hashCode("a"), caseSensitiveLocalComparer.hashCode("A"));
        assertTrue(caseSensitiveLocalComparer.compare(null, null) == 0);
        assertTrue(caseSensitiveLocalComparer.compare(null, "a") < 0);
        assertTrue(caseSensitiveLocalComparer.compare("a", null) > 0);
        assertTrue(caseSensitiveLocalComparer.compare("编辑", "测试") > 0);

        StringComparer ignoreCaseLocalComparer = StringComparer.OrdinalIgnoreCase;
        assertTrue(ignoreCaseLocalComparer.equals(null, null));
        assertFalse(ignoreCaseLocalComparer.equals(null, "a"));
        assertFalse(ignoreCaseLocalComparer.equals("a", null));
        assertTrue(ignoreCaseLocalComparer.equals("a", "A"));
        assertEquals(0, ignoreCaseLocalComparer.hashCode(null));
        assertEquals(ignoreCaseLocalComparer.hashCode("a"), ignoreCaseLocalComparer.hashCode("A"));
        assertTrue(ignoreCaseLocalComparer.compare(null, null) == 0);
        assertTrue(ignoreCaseLocalComparer.compare(null, "a") < 0);
        assertTrue(ignoreCaseLocalComparer.compare("a", null) > 0);
        assertTrue(ignoreCaseLocalComparer.compare("编辑", "测试") > 0);
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
