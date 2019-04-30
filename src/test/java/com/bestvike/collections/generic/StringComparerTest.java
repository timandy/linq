package com.bestvike.collections.generic;

import com.bestvike.CultureInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

/**
 * Created by 许崇雷 on 2019-04-30.
 */
public class StringComparerTest {
    @Before
    public void before() {
        CultureInfo.setCurrent(Locale.CHINA);
        Assert.assertEquals(Locale.ROOT, CultureInfo.getInvariant());
    }

    @Test
    public void testEquals() {
        String a = "abc123你好";
        String b = "AbC123你好";
        Assert.assertFalse(StringComparer.CurrentCulture.equals(a, b));
        Assert.assertTrue(StringComparer.CurrentCultureIgnoreCase.equals(a, b));
        Assert.assertFalse(StringComparer.InvariantCulture.equals(a, b));
        Assert.assertTrue(StringComparer.InvariantCultureIgnoreCase.equals(a, b));
        Assert.assertFalse(StringComparer.Ordinal.equals(a, b));
        Assert.assertTrue(StringComparer.OrdinalIgnoreCase.equals(a, b));
    }

    @Test
    public void testHashCode() {
        String a = "abc123你好";
        String b = "AbC123你好";
        Assert.assertNotEquals(StringComparer.CurrentCulture.hashCode(a), StringComparer.CurrentCulture.hashCode(b));
        Assert.assertEquals(StringComparer.CurrentCultureIgnoreCase.hashCode(a), StringComparer.CurrentCultureIgnoreCase.hashCode(b));
        Assert.assertNotEquals(StringComparer.InvariantCulture.hashCode(a), StringComparer.InvariantCulture.hashCode(b));
        Assert.assertEquals(StringComparer.InvariantCultureIgnoreCase.hashCode(a), StringComparer.InvariantCultureIgnoreCase.hashCode(b));
        Assert.assertNotEquals(StringComparer.Ordinal.hashCode(a), StringComparer.Ordinal.hashCode(b));
        Assert.assertEquals(StringComparer.OrdinalIgnoreCase.hashCode(a), StringComparer.OrdinalIgnoreCase.hashCode(b));
    }

    @Test
    public void testCompare() {
        String a = "abc123你好";
        String b = "AbC123你好";
        Assert.assertTrue(StringComparer.CurrentCulture.compare(a, b) < 0);
        Assert.assertTrue(StringComparer.CurrentCultureIgnoreCase.compare(a, b) == 0);
        Assert.assertTrue(StringComparer.InvariantCulture.compare(a, b) < 0);
        Assert.assertTrue(StringComparer.InvariantCultureIgnoreCase.compare(a, b) == 0);
        Assert.assertTrue(StringComparer.Ordinal.compare(a, b) > 0);
        Assert.assertTrue(StringComparer.OrdinalIgnoreCase.compare(a, b) == 0);

        a = "李四 123 abc";
        b = "张三 123 ABC";
        Assert.assertTrue(StringComparer.CurrentCulture.compare(a, b) < 0);
        Assert.assertTrue(StringComparer.CurrentCultureIgnoreCase.compare(a, b) < 0);
        Assert.assertTrue(StringComparer.InvariantCulture.compare(a, b) > 0);
        Assert.assertTrue(StringComparer.InvariantCultureIgnoreCase.compare(a, b) > 0);
        Assert.assertTrue(StringComparer.Ordinal.compare(a, b) > 0);
        Assert.assertTrue(StringComparer.OrdinalIgnoreCase.compare(a, b) > 0);
    }
}
