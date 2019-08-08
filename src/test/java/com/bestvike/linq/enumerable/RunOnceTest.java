package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IArrayList;
import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.RepeatInvokeException;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

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
        assertThrows(RepeatInvokeException.class, () -> source.firstOrDefault());
    }

    @Test
    public void testRunOnceLinkedList() {
        IEnumerable<Integer> q = Linq.of(new LinkedList<>(Arrays.asList(0, 1, 2, 3)));
        assertTrue(q.runOnce() instanceof RunOnceLinkedList);
        assertTrue(RunOnce.runOnce(q) instanceof RunOnceLinkedList);
        IEnumerable<Integer> source = q.runOnce();
        assertEquals(0, source.firstOrDefault());
        assertThrows(RepeatInvokeException.class, () -> source.firstOrDefault());

        IList<Integer> q2 = (IList<Integer>) Linq.of(new LinkedList<>(Arrays.asList(0, 1, 2, 3)));
        assertTrue(q2.runOnce() instanceof RunOnceLinkedList);
        assertTrue(RunOnce.runOnce(q2) instanceof RunOnceLinkedList);
        IEnumerable<Integer> source2 = q2.runOnce();
        assertEquals(0, source2.firstOrDefault());
        assertThrows(RepeatInvokeException.class, () -> source2.firstOrDefault());
        IEnumerable<Integer> source3 = q2.runOnce();
        assertEquals(2, source3.elementAt(2));
        assertThrows(RepeatInvokeException.class, () -> source3.elementAt(2));
    }

    @Test
    public void testRunOnceArrayList() {
        IEnumerable<Integer> q = Linq.of(0, 1, 2, 3);
        assertTrue(q.runOnce() instanceof RunOnceArrayList);
        assertTrue(RunOnce.runOnce(q) instanceof RunOnceArrayList);
        IEnumerable<Integer> source = q.runOnce();
        assertEquals(2, source.indexOf(2));
        assertThrows(RepeatInvokeException.class, () -> source.firstOrDefault());

        IArrayList<Integer> q2 = (IArrayList<Integer>) Linq.of(0, 1, 2, 3);
        assertTrue(q2.runOnce() instanceof RunOnceArrayList);
        assertTrue(RunOnce.runOnce(q2) instanceof RunOnceArrayList);
        IEnumerable<Integer> source2 = q2.runOnce();
        assertEquals(2, source2.indexOf(2));
        assertThrows(RepeatInvokeException.class, () -> source2.firstOrDefault());
        IEnumerable<Integer> source3 = q2.runOnce();
        assertEquals(2, source3.elementAt(2));
        assertThrows(RepeatInvokeException.class, () -> source3.elementAt(2));
    }

    @Test
    public void testOthers() {
        assertEquals(Linq.of(0, 1, 2, 3), Linq.of(0, 1).concat(Linq.of(2, 3).runOnce()).toArray());
        assertEquals(Arrays.asList(0, 1, 2, 3), Linq.of(0, 1).concat(Linq.of(2, 3).runOnce()).toList());
        assertEquals(Linq.of(0, 1, 2, 3), Linq.of(Linq.of(0, 1, 2, 3).runOnce().toArray(Integer.class)));
        assertEquals(Arrays.asList(0, 1, 2, 3), Linq.of(0, 1, 2, 3).runOnce().toList());
    }
}
