package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-05-27.
 */
public class ShuffleTest extends TestCase {
    @Test
    public void testShuffle() {
        IEnumerable<Integer> source = Linq.range(0, 100);
        Array<Integer> sourceArray = source.toArray();
        assertNotEquals(sourceArray, source.shuffle());
        assertNotEquals(sourceArray, source.shuffle(1));
        assertEquals(100, source.shuffle(1).count());
    }
}
