package com.bestvike.linq.util.lang;

import com.bestvike.TestCase;
import com.bestvike.linq.enumerable.Values;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.util.Strings;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2020-07-24.
 */
class StringSpanTest extends TestCase {
    @Test
    void testAsSpan() {
        assertThrows(ArgumentNullException.class, () -> StringSpan.asSpan(null));
        assertSame(StringSpan.Empty, StringSpan.asSpan(Strings.Empty));
        IStringSpan span = StringSpan.asSpan("abc");
        assertIsType(StringSpan.class, span);
        assertEquals("abc", span.toString());
    }

    @Test
    void testAsSpan1() {
        assertThrows(ArgumentNullException.class, () -> StringSpan.asSpan(null, 1));
        assertIsType(StringSubSpan.class, StringSpan.asSpan("abc", 1));
        assertEquals("bc", StringSpan.asSpan("abc", 1).toString());
    }

    @Test
    void testAsSpan2() {
        assertThrows(ArgumentNullException.class, () -> StringSpan.asSpan(null, 1, 2));
        assertThrows(ArgumentOutOfRangeException.class, () -> StringSpan.asSpan("abc", -1, 2));
        assertThrows(ArgumentOutOfRangeException.class, () -> StringSpan.asSpan("abc", 4, 2));
        assertThrows(ArgumentOutOfRangeException.class, () -> StringSpan.asSpan("abc", 1, -1));
        assertThrows(ArgumentOutOfRangeException.class, () -> StringSpan.asSpan("abc", 3, 2));

        assertSame(StringSpan.Empty, StringSpan.asSpan("abc", 1, 0));
        assertIsType(StringSpan.class, StringSpan.asSpan("abc", 0, 3));
        String source = "abc";
        assertSame(source, StringSpan.asSpan(source, 0, 3).toString());
        assertIsType(StringSubSpan.class, StringSpan.asSpan("abc", 1, 2));
        assertEquals("bc", StringSpan.asSpan("abc", 1, 2).toString());
    }

    @Test
    void value() {
        String value = "abc";
        IStringSpan source = new StringSpan(value);
        assertSame(value, source.raw());
    }

    @Test
    void offset() {
        String value = "abc";
        IStringSpan source = new StringSpan(value);
        assertEquals(0, source.offset());
    }

    @Test
    void length() {
        String value = "abc";
        IStringSpan source = new StringSpan(value);
        assertEquals(3, source.length());
    }

    @Test
    void charAt() {
        String value = "abc";
        IStringSpan source = new StringSpan(value);
        assertEquals('c', source.charAt(2));
    }

    @Test
    void trim() {
        String value = "　\t abc　\t ";
        IStringSpan source = new StringSpan(value);
        assertEquals("abc", source.trim().toString());
    }

    @Test
    void subSequence() {
        String value = "abcd";
        IStringSpan source = new StringSpan(value);
        assertSame(source, source.subSequence(0));
        assertEquals("abcd", source.subSequence(0).toString());
        assertEquals("bcd", source.subSequence(1).toString());
        assertEquals("", source.subSequence(4).toString());

        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(-1));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(5));
    }

    @Test
    void testSubSequence() {
        String value = "abcd";
        IStringSpan source = new StringSpan(value);
        assertSame(source, source.subSequence(0, 4));
        assertEquals("b", source.subSequence(1, 2).toString());
        assertEquals("", source.subSequence(4, 4).toString());

        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(-1, 0));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(5, 0));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(0, 5));
    }

    @Test
    void testEquals() {
        String value = "abcd";
        IStringSpan source = new StringSpan(value);

        assertTrue(source.equals(source));
        assertFalse(source.equals("abcd"));
        assertFalse(source.equals(new StringSpan("abc")));
        assertFalse(source.equals(new StringSpan("abdd")));
        assertTrue(source.equals(new StringSpan("abcd")));
    }

    @Test
    void testHashCode() {
        String value = "abcd";
        IStringSpan source = new StringSpan(value);

        assertEquals(Values.hashCode("abcd".toCharArray()), source.hashCode());
    }

    @Test
    void testToString() {
        String value = "abcd";
        IStringSpan source = new StringSpan(value);

        assertEquals("abcd", source.toString());
    }
}
