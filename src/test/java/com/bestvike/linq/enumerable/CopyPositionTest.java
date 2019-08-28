package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-08-02.
 */
public class CopyPositionTest extends TestCase {
    @Test
    public void testEquals() {
        CopyPosition expected = new CopyPosition(1, 2);
        CopyPosition copyPosition = new CopyPosition(1, 2);
        assertEquals(expected, copyPosition);
    }

    @Test
    public void testHashCode() {
        CopyPosition expected = new CopyPosition(1, 2);
        CopyPosition copyPosition = new CopyPosition(1, 2);
        assertEquals(expected.hashCode(), copyPosition.hashCode());
    }

    @Test
    public void testToString() {
        CopyPosition expected = new CopyPosition(1, 2);
        CopyPosition copyPosition = new CopyPosition(1, 2);
        assertEquals(expected.toString(), copyPosition.toString());
    }
}
