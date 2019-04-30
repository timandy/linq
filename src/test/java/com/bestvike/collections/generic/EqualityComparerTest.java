package com.bestvike.collections.generic;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-04-30.
 */
public class EqualityComparerTest {
    @Test
    public void testDefault() {
        Assert.assertEquals(EqualityComparer.Default(), EqualityComparer.Default());
    }

    @Test
    public void testEquals() {
        String a = "abc";
        //noinspection StringOperationCanBeSimplified
        String b = new String("abc");
        Assert.assertNotSame(a, b);
        Assert.assertTrue(EqualityComparer.Default().equals(a, b));
        Assert.assertFalse(EqualityComparer.Default().equals(a, "def"));
    }

    @Test
    public void testHashCode() {
        String a = "abc";
        //noinspection StringOperationCanBeSimplified
        String b = new String("abc");
        Assert.assertNotSame(a, b);
        Assert.assertEquals(0, EqualityComparer.Default().hashCode(null));
        Assert.assertEquals(EqualityComparer.Default().hashCode(a), EqualityComparer.Default().hashCode(b));
    }

    @Test
    public void testIndexOf() {
        String[] array = new String[]{"Hello", " ", "tim", null, "Bye", "Bye", null};
        Assert.assertEquals(2, EqualityComparer.Default().indexOf(array, "tim", 0, array.length));
        Assert.assertEquals(2, EqualityComparer.Default().indexOf(array, "tim", 2, array.length - 2));
        Assert.assertEquals(-1, EqualityComparer.Default().indexOf(array, "tim", 3, array.length - 3));
        Assert.assertEquals(3, EqualityComparer.Default().indexOf(array, null, 0, array.length));
        Assert.assertEquals(4, EqualityComparer.Default().indexOf(array, "Bye", 0, array.length));
    }

    @Test
    public void testLastIndexOf() {
        String[] array = new String[]{"Hello", " ", "tim", null, "Bye", "Bye", null};
        Assert.assertEquals(2, EqualityComparer.Default().lastIndexOf(array, "tim", 6, 7));
        Assert.assertEquals(2, EqualityComparer.Default().lastIndexOf(array, "tim", 2, 3));
        Assert.assertEquals(-1, EqualityComparer.Default().lastIndexOf(array, "tim", 1, 2));
        Assert.assertEquals(6, EqualityComparer.Default().lastIndexOf(array, null, 6, 7));
        Assert.assertEquals(5, EqualityComparer.Default().lastIndexOf(array, "Bye", 6, 7));
    }
}
