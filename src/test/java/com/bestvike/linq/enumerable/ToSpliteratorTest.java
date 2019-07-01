package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.NotSupportedException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by 许崇雷 on 2019-04-25.
 */
public class ToSpliteratorTest extends TestCase {
    @Test
    public void testReset() {
        IEnumerator<Employee> enumerator = Linq.of(emps).enumerator();
        assertThrows(NotSupportedException.class, enumerator::reset);
    }

    @Test
    public void testRemove() {
        IEnumerator<Employee> enumerator = Linq.of(emps).enumerator();
        enumerator.moveNext();
        assertThrows(NotSupportedException.class, enumerator::remove);
    }

    @Test
    public void testForEachRemaining() {
        List<String> ids = new ArrayList<>();
        try (IEnumerator<Employee> enumerator = Linq.of(emps).enumerator()) {
            enumerator.moveNext();
            enumerator.moveNext();
            enumerator.forEachRemaining(a -> ids.add(String.valueOf(a.empno)));
        }
        assertEquals(2, ids.size());
        assertEquals("120", ids.get(0));
        assertEquals("130", ids.get(1));
    }

    @Test
    public void testForEach() {
        List<String> ids = new ArrayList<>();
        IEnumerable<Employee> enumerable = Linq.of(emps);
        enumerable.forEach(a -> ids.add(String.valueOf(a.empno)));
        assertEquals(4, ids.size());
        assertEquals("100", ids.get(0));
        assertEquals("110", ids.get(1));
        assertEquals("120", ids.get(2));
        assertEquals("130", ids.get(3));
    }

    @Test
    public void testStream() {
        Stream<Integer> stream = Linq.range(0, 2048).stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.range(1024, 1024));
    }

    @Test
    public void testStreamInt() {
        int[] array = {2, 4, 6, 7};
        IEnumerable<Integer> enumerable = Linq.of(array);
        Stream<Integer> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.of(2, 4, 6));
    }

    @Test
    public void testStreamLong() {
        long[] array = {2, 4, 6, 7};
        IEnumerable<Long> enumerable = Linq.of(array);
        Stream<Long> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Long.class), Linq.of(2L, 4L, 6L));
    }

    @Test
    public void testStreamDouble() {
        double[] array = {2d, 4d, 6d, 7d};
        IEnumerable<Double> enumerable = Linq.of(array);
        Stream<Double> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.of(objects).cast(Double.class), Linq.of(2d, 4d, 6d));
    }

    @Test
    public void testStream2() {
        Stream<Integer> stream = Linq.range(0, 2048).stream(false);
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.range(1024, 1024));
        //
        Stream<Integer> pStream = Linq.range(0, 2048).stream(true);
        assertTrue(pStream.isParallel());
        Object[] pObjects = pStream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.of(pObjects).cast(Integer.class), Linq.range(1024, 1024));
    }

    @Test
    public void testStreamInt2() {
        int[] array = {2, 4, 6, 7};
        IEnumerable<Integer> enumerable = Linq.of(array);
        Stream<Integer> stream = enumerable.stream(false);
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.of(2, 4, 6));
        //
        Stream<Integer> pStream = enumerable.stream(true);
        assertTrue(pStream.isParallel());
        Object[] PObjects = pStream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(PObjects).cast(Integer.class), Linq.of(2, 4, 6));
    }

    @Test
    public void testStreamLong2() {
        long[] array = {2, 4, 6, 7};
        IEnumerable<Long> enumerable = Linq.of(array);
        Stream<Long> stream = enumerable.stream(false);
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Long.class), Linq.of(2L, 4L, 6L));
        //
        Stream<Long> pStream = enumerable.stream(true);
        assertTrue(pStream.isParallel());
        Object[] pObjects = pStream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(pObjects).cast(Long.class), Linq.of(2L, 4L, 6L));
    }

    @Test
    public void testStreamDouble2() {
        double[] array = {2d, 4d, 6d, 7d};
        IEnumerable<Double> enumerable = Linq.of(array);
        Stream<Double> stream = enumerable.stream(false);
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.of(objects).cast(Double.class), Linq.of(2d, 4d, 6d));
        //
        Stream<Double> pStream = enumerable.stream(true);
        assertTrue(pStream.isParallel());
        Object[] pObjects = pStream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.of(pObjects).cast(Double.class), Linq.of(2d, 4d, 6d));
    }

    @Test
    public void testStream3() {
        Stream<Integer> stream = Linq.range(0, 2048).parallelStream();
        assertTrue(stream.isParallel());
        Object[] objects = stream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.range(1024, 1024));
    }

    @Test
    public void testStreamInt3() {
        int[] array = {2, 4, 6, 7};
        IEnumerable<Integer> enumerable = Linq.of(array);
        Stream<Integer> stream = enumerable.parallelStream();
        assertTrue(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.of(2, 4, 6));
    }

    @Test
    public void testStreamLong3() {
        long[] array = {2, 4, 6, 7};
        IEnumerable<Long> enumerable = Linq.of(array);
        Stream<Long> stream = enumerable.parallelStream();
        assertTrue(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Long.class), Linq.of(2L, 4L, 6L));
    }

    @Test
    public void testStreamDouble3() {
        double[] array = {2d, 4d, 6d, 7d};
        IEnumerable<Double> enumerable = Linq.of(array);
        Stream<Double> stream = enumerable.parallelStream();
        assertTrue(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.of(objects).cast(Double.class), Linq.of(2d, 4d, 6d));
    }
}
