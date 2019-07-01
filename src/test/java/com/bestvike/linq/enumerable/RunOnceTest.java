package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.NotSupportedException;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-05-27.
 */
public class RunOnceTest extends TestCase {
    @Test
    public void testRunOnceEnumerable() {
        IEnumerable<Integer> q = Linq.range(0, 10);
        assertTrue(q.runOnce() instanceof RunOnceEnumerable);
        assertTrue(RunOnce.runOnce(q) instanceof RunOnceEnumerable);
        IEnumerable<Integer> source = q.runOnce();
        assertEquals(0, source.firstOrDefault());
        assertThrows(NotSupportedException.class, () -> source.firstOrDefault());
    }

    @Test
    public void testRunOnceList() {
        IEnumerable<Integer> q = Linq.of(0, 1, 2, 3);
        assertTrue(q.runOnce() instanceof RunOnceList);
        assertTrue(RunOnce.runOnce(q) instanceof RunOnceList);
        IEnumerable<Integer> source = q.runOnce();
        assertEquals(0, source.firstOrDefault());
        assertThrows(NotSupportedException.class, () -> source.firstOrDefault());

        IList<Integer> q2 = (IList<Integer>) Linq.of(0, 1, 2, 3);
        assertTrue(q2.runOnce() instanceof RunOnceList);
        assertTrue(RunOnce.runOnce(q2) instanceof RunOnceList);
        IEnumerable<Integer> source2 = q2.runOnce();
        assertEquals(0, source2.firstOrDefault());
        assertThrows(NotSupportedException.class, () -> source2.firstOrDefault());
    }
}
