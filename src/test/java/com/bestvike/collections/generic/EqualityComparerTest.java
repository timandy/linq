package com.bestvike.collections.generic;

import com.bestvike.TestCase;
import com.bestvike.function.Predicate2;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-04-30.
 */
public class EqualityComparerTest extends TestCase {
    @Test
    public void testDefault() {
        assertSame(EqualityComparer.Default(), EqualityComparer.Default());
        Predicate2<String, String> predicate = EqualityComparer.<String>Default()::equals;
        assertTrue(predicate.apply("hello", "hello"));
        assertFalse(predicate.apply("hello", "world"));
    }

    @Test
    public void testEquals() {
        String a = "abc";
        //noinspection StringOperationCanBeSimplified
        String b = new String("abc");
        assertNotSame(a, b);
        assertTrue(EqualityComparer.Default().equals(a, b));
        assertFalse(EqualityComparer.Default().equals(a, "def"));
    }

    @Test
    public void testHashCode() {
        String a = "abc";
        //noinspection StringOperationCanBeSimplified
        String b = new String("abc");
        assertNotSame(a, b);
        assertEquals(0, EqualityComparer.Default().hashCode(null));
        assertEquals(EqualityComparer.Default().hashCode(a), EqualityComparer.Default().hashCode(b));
    }

    @Test
    public void testIndexOf() {
        String[] array = new String[]{"Hello", " ", "tim", null, "Bye", "Bye", null};
        assertEquals(2, EqualityComparer.Default().indexOf(array, "tim", 0, array.length));
        assertEquals(2, EqualityComparer.Default().indexOf(array, "tim", 2, array.length - 2));
        assertEquals(-1, EqualityComparer.Default().indexOf(array, "tim", 3, array.length - 3));
        assertEquals(3, EqualityComparer.Default().indexOf(array, null, 0, array.length));
        assertEquals(4, EqualityComparer.Default().indexOf(array, "Bye", 0, array.length));
    }

    @Test
    public void testLastIndexOf() {
        String[] array = new String[]{"Hello", " ", "tim", null, "Bye", "Bye", null};
        assertEquals(2, EqualityComparer.Default().lastIndexOf(array, "tim", 6, 7));
        assertEquals(2, EqualityComparer.Default().lastIndexOf(array, "tim", 2, 3));
        assertEquals(-1, EqualityComparer.Default().lastIndexOf(array, "tim", 1, 2));
        assertEquals(6, EqualityComparer.Default().lastIndexOf(array, null, 6, 7));
        assertEquals(5, EqualityComparer.Default().lastIndexOf(array, "Bye", 6, 7));
    }
}
