package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-08-02.
 */
public class ArrayBuilderTest extends TestCase {
    @Test
    public void test() {
        ArrayBuilder<Integer> arrayBuilder = new ArrayBuilder<>(1);
        arrayBuilder.add(1);
        arrayBuilder.add(2);
        arrayBuilder.set(1, 3);
        assertEquals(Linq.of(1, 3), Linq.of(arrayBuilder.toArray()));
        assertEquals(Linq.of(1, 3), Linq.of(arrayBuilder.toArray(Integer.class)));
    }

    @Test
    public void testEmpty() {
        ArrayBuilder<Integer> arrayBuilder = new ArrayBuilder<>(0);
        assertEquals(Linq.empty(), Linq.of(arrayBuilder.toArray()));
        assertEquals(Linq.empty(), Linq.of(arrayBuilder.toArray(Integer.class)));
    }
}
