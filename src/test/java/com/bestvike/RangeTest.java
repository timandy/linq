package com.bestvike;

import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2023-05-31.
 */
class RangeTest extends TestCase {
    @Test
    void CreationTest() {
        Range range = new Range(new Index(10, false), new Index(2, true));
        assertEquals(10, range.getStart().getValue());
        assertFalse(range.getStart().isFromEnd());
        assertEquals(2, range.getEnd().getValue());
        assertTrue(range.getEnd().isFromEnd());

        range = Range.startAt(new Index(7, false));
        assertEquals(7, range.getStart().getValue());
        assertFalse(range.getStart().isFromEnd());
        assertEquals(0, range.getEnd().getValue());
        assertTrue(range.getEnd().isFromEnd());

        range = Range.endAt(new Index(3, true));
        assertEquals(0, range.getStart().getValue());
        assertFalse(range.getStart().isFromEnd());
        assertEquals(3, range.getEnd().getValue());
        assertTrue(range.getEnd().isFromEnd());

        range = Range.All;
        assertEquals(0, range.getStart().getValue());
        assertFalse(range.getStart().isFromEnd());
        assertEquals(0, range.getEnd().getValue());
        assertTrue(range.getEnd().isFromEnd());
    }

    @Test
    void GetOffsetAndLengthTest() {
        Range range = Range.startAt(Index.fromStart(5));
        Range.OffsetLength offsetLength = range.getOffsetAndLength(20);
        assertEquals(5, offsetLength.Offset);
        assertEquals(15, offsetLength.Length);

        offsetLength = range.getOffsetAndLength(5);
        assertEquals(5, offsetLength.Offset);
        assertEquals(0, offsetLength.Length);

        // we don't validate the length in the GetOffsetAndLength so passing negative length will just return the regular calculation according to the length value.
        offsetLength = range.getOffsetAndLength(-10);
        assertEquals(5, offsetLength.Offset);
        assertEquals(-15, offsetLength.Length);

        assertThrows(ArgumentOutOfRangeException.class, () -> {
            range.getOffsetAndLength(4);
        });

        Range range2 = Range.endAt(Index.fromStart(4));
        offsetLength = range2.getOffsetAndLength(20);
        assertEquals(0, offsetLength.Offset);
        assertEquals(4, offsetLength.Length);
        assertThrows(ArgumentOutOfRangeException.class, () -> {
            range2.getOffsetAndLength(1);
        });
    }

    @Test
    void EqualityTest() {
        Range range1 = new Range(new Index(10, false), new Index(20, false));
        Range range2 = new Range(new Index(10, false), new Index(20, false));
        assertTrue(range1.equals(range2));

        range2 = new Range(new Index(10, false), new Index(20, true));
        assertFalse(range1.equals(range2));

        range2 = new Range(new Index(10, false), new Index(21, false));
        assertFalse(range1.equals(range2));
    }

    @Test
    void HashCodeTest() {
        Range range1 = new Range(new Index(10, false), new Index(20, false));
        Range range2 = new Range(new Index(10, false), new Index(20, false));
        assertEquals(range1.hashCode(), range2.hashCode());

        range2 = new Range(new Index(10, false), new Index(20, true));
        assertNotEquals(range1.hashCode(), range2.hashCode());

        range2 = new Range(new Index(10, false), new Index(21, false));
        assertNotEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void CloneTest() {
        Range range1 = new Range(new Index(10, false), new Index(20, true));
        assertEquals(range1, range1.clone());
    }

    @Test
    void ToStringTest() {
        Range range1 = new Range(new Index(10, false), new Index(20, false));
        assertEquals("10..20", range1.toString());

        range1 = new Range(new Index(10, false), new Index(20, true));
        assertEquals("10..^20", range1.toString());
    }
}
