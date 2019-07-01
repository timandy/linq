package com.bestvike.collections.generic;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-06-20.
 */
public class ArrayTest extends TestCase {
    @Test
    public void testNotCopy() {
        String[] objects = {"hello", "world"};
        Array<String> source = new Array<>(objects);

        assertSame(objects, source.getArray());
    }

    @Test
    public void testNullElementsToCreate() {
        assertThrows(ArgumentNullException.class, () -> new Array<>(null));
    }

    @Test
    public void testContains() {
        Array<String> source = new Array<>(new String[]{"hello", "world"});

        assertTrue(source._contains("hello"));
        assertFalse(source._contains(null));
    }

    @Test
    public void testToArray() {
        String[] objects = {"hello", "world"};
        Array<String> source = new Array<>(objects);
        String[] result = source._toArray(String.class);

        assertNotSame(objects, result);
        assertEquals(Linq.of(objects), Linq.of(result));
    }

    @Test
    public void testClone() {
        String[] objects = {"hello", "world"};
        Array<String> source = new Array<>(objects);
        Array<String> result = source.clone();

        assertNotSame(source, result);
        assertNotSame(source.getArray(), result.getArray());
        assertEquals(source, result);
    }
}
