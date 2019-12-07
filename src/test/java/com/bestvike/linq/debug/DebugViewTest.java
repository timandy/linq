package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-12-07.
 */
class DebugViewTest extends TestCase {
    @Test
    void testCopyPosition() throws Exception {
        Constructor<?> ctor = Class.forName("com.bestvike.linq.enumerable.CopyPosition").getDeclaredConstructor(int.class, int.class);
        ctor.setAccessible(true);
        Object copyPosition = ctor.newInstance(1, 2);
        assertEquals("[1, 2]", DebugView.getDebuggerDisplay(copyPosition));
    }

    @Test
    void testMarker() throws Exception {
        Constructor<?> ctor = Class.forName("com.bestvike.linq.enumerable.Marker").getDeclaredConstructor(int.class, int.class);
        ctor.setAccessible(true);
        Object marker = ctor.newInstance(1, 2);
        assertEquals("index: 2, count: 1", DebugView.getDebuggerDisplay(marker));
    }

    @Test
    void testEmptyPartition() throws Exception {
        IEnumerable<Object> emptyPartition = Linq.empty();
        assertIsType(Class.forName("com.bestvike.linq.enumerable.EmptyPartition"), emptyPartition);
        assertEquals("Count = 0", DebugView.getDebuggerDisplay(emptyPartition));
    }

    @Test
    void testListPartition() throws Exception {
        IEnumerable<Integer> listPartition = Linq.of(1, 2, 3).skip(1);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.ListPartition"), listPartition);
        assertEquals("Count = 2", DebugView.getDebuggerDisplay(listPartition));
    }

    @Test
    void testIListPartition() throws Exception {
        IEnumerable<Integer> iListPartition = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3))).skip(1);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.IListPartition"), iListPartition);
        assertEquals("Count = 2", DebugView.getDebuggerDisplay(iListPartition));
    }

    @Test
    void testRange() throws Exception {
        IEnumerable<Integer> range = Linq.range(0, 3);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.RangeIterator"), range);
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(range));
    }

    @Test
    void testRepeat() throws Exception {
        IEnumerable<Integer> repeat = Linq.repeat(1, 3);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.RepeatIterator"), repeat);
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(repeat));
    }

    @Test
    void testSelectArray() throws Exception {
        IEnumerable<Integer> source = Linq.of(1, 2, 3).select(x -> x);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.SelectArrayIterator"), source);
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testSelectRange() throws Exception {
        IEnumerable<Integer> source = Linq.range(0, 3).select(x -> x);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.SelectRangeIterator"), source);
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testSelectRepeat() throws Exception {
        IEnumerable<Integer> source = Linq.repeat(1, 3).select(x -> x);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.SelectRepeatIterator"), source);
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testSelectList() throws Exception {
        IEnumerable<Integer> source = Linq.of(Arrays.asList(1, 2, 3)).select(x -> x);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.SelectListIterator"), source);
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testSelectIList() throws Exception {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3))).select(x -> x);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.SelectIListIterator"), source);
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testSelectListPartition() throws Exception {
        IEnumerable<Integer> source = Linq.of(Arrays.asList(1, 2, 3)).skip(1).select(x -> x);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.SelectListPartitionIterator"), source);
        assertEquals("Count = 2", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testSelectIListPartition() throws Exception {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3))).skip(1).select(x -> x);
        assertIsType(Class.forName("com.bestvike.linq.enumerable.SelectIListPartitionIterator"), source);
        assertEquals("Count = 2", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testCollection() {
        List<Integer> source = Arrays.asList(1, 2, 3);
        assertIsAssignableFrom(Collection.class, source);
        assertNull(source.getClass().getAnnotation(DebuggerDisplay.class));
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testICollection() {
        IEnumerable<Integer> source = Linq.of(1, 2, 3);
        assertIsAssignableFrom(ICollection.class, source);
        assertNull(source.getClass().getAnnotation(DebuggerDisplay.class));
        assertEquals("Count = 3", DebugView.getDebuggerDisplay(source));
    }

    @Test
    void testObject() {
        Object source = new Object();
        assertEquals(source.getClass().getName(), DebugView.getDebuggerDisplay(source));
        assertSame(source, DebugView.getDebuggerProxyObject(source));
    }
}
