package com.bestvike.collections.generic;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-06-20.
 */
class ArrayTest extends TestCase {
    @Test
    void testNotCopy() {
        String[] objects = {"hello", "world"};
        Array<String> source = new Array<>(objects);

        assertSame(objects, source.getArray());
    }

    @Test
    void testNullElementsToCreate() {
        assertThrows(ArgumentNullException.class, () -> new Array<>(null));
    }

    @Test
    void testContains() {
        Array<String> source = new Array<>(new String[]{"hello", "world"});

        assertTrue(source._contains("hello"));
        assertFalse(source._contains(null));
    }

    @Test
    void testToArray() {
        String[] objects = {"hello", "world"};
        Array<String> source = new Array<>(objects);
        String[] result = source._toArray(String.class);

        assertNotSame(objects, result);
        assertEquals(Linq.of(objects), Linq.of(result));
    }

    @Test
    void testClone() {
        String[] objects = {"hello", "world"};
        Array<String> source = new Array<>(objects);
        Array<String> result = source.clone();

        assertNotSame(source, result);
        assertNotSame(source.getArray(), result.getArray());
        assertEquals(source, result);
    }

    @Test
    void testSize() {
        Array<Integer> empty = Linq.<Integer>empty().toArray();
        assertEquals(0, empty.size());

        Array<Integer> source = Linq.of(0, 1).toArray();
        assertEquals(2, source.size());
    }

    @Test
    void testIsEmpty() {
        Array<Integer> empty = Linq.<Integer>empty().toArray();
        assertTrue(empty.isEmpty());

        Array<Integer> source = Linq.of(0, 1).toArray();
        assertFalse(source.isEmpty());
    }
}
