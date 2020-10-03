package com.bestvike.linq.util.lang;

import com.bestvike.TestCase;
import com.bestvike.linq.enumerable.Values;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2020-07-24.
 */
class StringSubSpanTest extends TestCase {
    @Test
    void value() {
        String value = " abc ";
        IStringSpan source = new StringSubSpan(value, 1, 3);
        assertSame(value, source.raw());
    }

    @Test
    void offset() {
        String value = " abc ";
        IStringSpan source = new StringSubSpan(value, 1, 3);
        assertEquals(1, source.offset());
    }

    @Test
    void length() {
        String value = " abc ";
        IStringSpan source = new StringSubSpan(value, 1, 3);
        assertEquals(3, source.length());
    }

    @Test
    void charAt() {
        String value = " abc ";
        IStringSpan source = new StringSubSpan(value, 1, 3);
        assertEquals('c', source.charAt(2));
    }

    @Test
    void trim() {
        String value = " 　\t abc　\t  ";
        IStringSpan source = new StringSubSpan(value, 1, 9);
        assertEquals("abc", source.trim().toString());
    }

    @Test
    void subSequence() {
        String value = " abcd ";
        IStringSpan source = new StringSubSpan(value, 1, 4);
        assertEquals("abcd", source.subSequence(0).toString());
        assertEquals("bcd", source.subSequence(1).toString());
        assertEquals("", source.subSequence(4).toString());

        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(-1));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(5));
    }

    @Test
    void testSubSequence() {
        String value = " abcd ";
        IStringSpan source = new StringSubSpan(value, 1, 4);
        assertEquals(value.substring(1, 3), source.subSequence(0, 2).toString());
        assertEquals("ab", source.subSequence(0, 2).toString());
        assertEquals("", source.subSequence(4, 4).toString());

        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(-1, 0));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(5, 0));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(0, 5));
    }

    @Test
    void testEquals() {
        String value = " abcd ";
        IStringSpan source = new StringSubSpan(value, 1, 4);

        assertFalse(source.equals("abcd"));
        assertFalse(source.equals(new StringSpan("abc")));
        assertFalse(source.equals(new StringSpan("abdd")));
        assertTrue(source.equals(new StringSpan("abcd")));
    }

    @Test
    void testHashCode() {
        String value = " abcd ";
        IStringSpan source = new StringSubSpan(value, 1, 4);

        assertEquals(Values.hashCode("abcd".toCharArray()), source.hashCode());
    }

    @Test
    void testToString() {
        String value = " abcd ";
        IStringSpan source = new StringSubSpan(value, 1, 4);

        assertEquals("abcd", source.toString());
    }
}
