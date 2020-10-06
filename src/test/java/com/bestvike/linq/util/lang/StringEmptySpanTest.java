package com.bestvike.linq.util.lang;

import com.bestvike.TestCase;
import com.bestvike.linq.enumerable.Values;
import com.bestvike.linq.util.Strings;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2020-07-24.
 */
class StringEmptySpanTest extends TestCase {
    @Test
    void value() {
        IStringSpan source = StringSpan.Empty;
        assertSame(Strings.Empty, source.raw());
    }

    @Test
    void offset() {
        IStringSpan source = StringSpan.Empty;
        assertEquals(0, source.offset());
    }

    @Test
    void length() {
        IStringSpan source = StringSpan.Empty;
        assertEquals(0, source.length());
    }

    @Test
    void charAt() {
        IStringSpan source = StringSpan.Empty;
        assertThrows(StringIndexOutOfBoundsException.class, () -> Strings.Empty.charAt(0));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.charAt(0));
    }

    @Test
    void trim() {
        IStringSpan source = StringSpan.Empty;
        assertSame(source, source.trim());
    }

    @Test
    void subSequence() {
        IStringSpan source = StringSpan.Empty;
        assertSame(source, source.subSequence(0));
        assertEquals(Strings.Empty, source.subSequence(0).toString());

        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(-1));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(1));
    }

    @Test
    void testSubSequence() {
        IStringSpan source = StringSpan.Empty;
        assertSame(source, source.subSequence(0, 0));

        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(-1, 0));
        assertThrows(StringIndexOutOfBoundsException.class, () -> source.subSequence(0, -1));
    }

    @Test
    void testEquals() {
        IStringSpan source = StringSpan.Empty;

        assertTrue(source.equals(source));
        assertFalse(source.equals("abcd"));
        assertTrue(source.equals(new StringSpan("")));
        assertFalse(source.equals(new StringSpan("a")));
    }

    @Test
    void testHashCode() {
        IStringSpan source = StringSpan.Empty;
        assertEquals(Values.hashCode("".toCharArray()), source.hashCode());
    }

    @Test
    void testToString() {
        IStringSpan source = StringSpan.Empty;
        assertEquals(Strings.Empty, source.toString());
    }
}
