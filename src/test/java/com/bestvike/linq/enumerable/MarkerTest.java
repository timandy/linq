package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-08-02.
 */
public class MarkerTest extends TestCase {
    @Test
    public void testEquals() {
        Marker expected = new Marker(1, 2);
        Marker marker = new Marker(1, 2);
        assertEquals(expected, marker);
    }

    @Test
    public void testHashCode() {
        Marker expected = new Marker(1, 2);
        Marker marker = new Marker(1, 2);
        assertEquals(expected.hashCode(), marker.hashCode());
    }

    @Test
    public void testToString() {
        Marker expected = new Marker(1, 2);
        Marker marker = new Marker(1, 2);
        assertEquals(expected.toString(), marker.toString());
    }
}
