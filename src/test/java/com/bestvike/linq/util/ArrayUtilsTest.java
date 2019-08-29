package com.bestvike.linq.util;

import com.bestvike.TestCase;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-07-12.
 */
public class ArrayUtilsTest extends TestCase {
    @Test
    public void indexOf() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.indexOf(null, 2));

        assertEquals(1, ArrayUtils.indexOf(new Integer[]{1, 2, 3}, 2));
        assertEquals(-1, ArrayUtils.indexOf(new Integer[]{1, 2, 3}, 4));
    }

    @Test
    public void indexOf2() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.indexOf(null, 1, 2, 2));
        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.indexOf(new Integer[]{1, 2, 3}, 1, -1, 2));
        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.indexOf(new Integer[]{1, 2, 3}, 1, 1, -1));

        assertEquals(1, ArrayUtils.indexOf(new Integer[]{1, 2, 3}, 2, 1, 2));
        assertEquals(-1, ArrayUtils.indexOf(new Integer[]{1, 2, 3}, 4, 1, 2));
    }

    @Test
    public void lastIndexOf() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.lastIndexOf(null, 2));

        assertEquals(2, ArrayUtils.lastIndexOf(new Integer[]{1, 2, 2}, 2));
        assertEquals(-1, ArrayUtils.lastIndexOf(new Integer[]{1, 2, 2}, 4));
    }

    @Test
    public void lastIndexOf2() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.lastIndexOf(null, 1, 2, 2));

        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.lastIndexOf(new Integer[]{}, 1, 1, 2));
        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.lastIndexOf(new Integer[]{}, 1, 0, 2));
        assertEquals(-1, ArrayUtils.lastIndexOf(new Integer[]{}, 1, 0, 0));

        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.lastIndexOf(new Integer[]{1, 2, 2}, 1, -1, 0));
        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.lastIndexOf(new Integer[]{1, 2, 2}, 1, 1, -1));

        assertEquals(2, ArrayUtils.lastIndexOf(new Integer[]{1, 2, 2}, 2, 2, 2));
        assertEquals(-1, ArrayUtils.lastIndexOf(new Integer[]{1, 2, 2}, 4, 2, 2));
    }

    @Test
    public void findIndex() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.findIndex(null, a -> true));

        assertEquals(1, ArrayUtils.findIndex(new Integer[]{1, 2, 3}, a -> a == 2));
        assertEquals(-1, ArrayUtils.findIndex(new Integer[]{1, 2, 3}, a -> a == 4));
    }

    @Test
    public void findIndex2() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.findIndex(null, 2, 2, a -> true));
        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.findIndex(new Integer[]{1, 2, 3}, -1, 2, a -> a == 1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.findIndex(new Integer[]{1, 2, 3}, 1, -1, a -> a == 1));
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.findIndex(new Integer[]{1, 2, 3}, 1, 2, null));

        assertEquals(1, ArrayUtils.findIndex(new Integer[]{1, 2, 3}, 1, 2, a -> a == 2));
        assertEquals(-1, ArrayUtils.findIndex(new Integer[]{1, 2, 3}, 1, 2, a -> a == 4));
    }

    @Test
    public void findLastIndex() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.findLastIndex(null, a -> true));
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.findLastIndex(new Integer[]{1, 2, 3}, null));

        assertEquals(2, ArrayUtils.findLastIndex(new Integer[]{1, 2, 2}, a -> a == 2));
        assertEquals(-1, ArrayUtils.findLastIndex(new Integer[]{1, 2, 2}, a -> a == 4));
    }

    @Test
    public void findLastIndex2() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.findLastIndex(null, 2, 2, a -> true));
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.findLastIndex(new Integer[]{}, 2, 2, null));

        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.findLastIndex(new Integer[]{}, 1, 2, a -> a == 1));

        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.findLastIndex(new Integer[]{1, 2, 2}, -1, 0, a -> a == 1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.findLastIndex(new Integer[]{1, 2, 2}, 1, -1, a -> a == 1));

        assertEquals(2, ArrayUtils.findLastIndex(new Integer[]{1, 2, 2}, 2, 3, a -> a == 2));
        assertEquals(-1, ArrayUtils.findLastIndex(new Integer[]{1, 2, 2}, 2, 3, a -> a == 4));
    }

    @Test
    public void testClone() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.clone(null));
    }

    @Test
    public void resize() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.resize(null, -1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ArrayUtils.resize(new Integer[]{}, -1));
    }

    @Test
    public void fill() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.fill(null, null));
    }

    @Test
    public void reverse() {
        assertThrows(ArgumentNullException.class, () -> ArrayUtils.reverse(null));
    }
}
