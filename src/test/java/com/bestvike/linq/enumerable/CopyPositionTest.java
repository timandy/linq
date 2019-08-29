package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-08-02.
 */
class CopyPositionTest extends TestCase {
    @Test
    void testEquals() {
        CopyPosition expected = new CopyPosition(1, 2);
        CopyPosition copyPosition = new CopyPosition(1, 2);
        assertEquals(expected, copyPosition);
    }

    @Test
    void testHashCode() {
        CopyPosition expected = new CopyPosition(1, 2);
        CopyPosition copyPosition = new CopyPosition(1, 2);
        assertEquals(expected.hashCode(), copyPosition.hashCode());
    }

    @Test
    void testToString() {
        CopyPosition expected = new CopyPosition(1, 2);
        CopyPosition copyPosition = new CopyPosition(1, 2);
        assertEquals(expected.toString(), copyPosition.toString());
    }
}
