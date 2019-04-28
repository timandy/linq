package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.NotSupportedException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by 许崇雷 on 2019-04-25.
 */
public class StreamTest extends EnumerableTest {
    @Test
    public void testReset() {
        final IEnumerator<Employee> enumerator = Linq.asEnumerable(emps).enumerator();
        assertThrows(NotSupportedException.class, enumerator::reset);
    }

    @Test
    public void testRemove() {
        final IEnumerator<Employee> enumerator = Linq.asEnumerable(emps).enumerator();
        enumerator.moveNext();
        assertThrows(NotSupportedException.class, enumerator::remove);
    }

    @Test
    public void testForEachRemaining() {
        List<String> ids = new ArrayList<>();
        try (IEnumerator<Employee> enumerator = Linq.asEnumerable(emps).enumerator()) {
            enumerator.moveNext();
            enumerator.moveNext();
            enumerator.forEachRemaining(a -> ids.add(String.valueOf(a.empno)));
        }
        Assert.assertEquals(2, ids.size());
        Assert.assertEquals("120", ids.get(0));
        Assert.assertEquals("130", ids.get(1));
    }

    @Test
    public void testForEach() {
        List<String> ids = new ArrayList<>();
        final IEnumerable<Employee> enumerable = Linq.asEnumerable(emps);
        enumerable.forEach(a -> ids.add(String.valueOf(a.empno)));
        Assert.assertEquals(4, ids.size());
        Assert.assertEquals("100", ids.get(0));
        Assert.assertEquals("110", ids.get(1));
        Assert.assertEquals("120", ids.get(2));
        Assert.assertEquals("130", ids.get(3));
    }

    @Test
    public void testStream() {
        final Stream<Integer> stream = Linq.range(0, 2048).stream();
        Assert.assertFalse(stream.isParallel());
        final Object[] objects = stream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Integer.class), Linq.range(1024, 1024));
    }

    @Test
    public void testStreamInt() {
        final int[] array = {2, 4, 6, 7};
        final IEnumerable<Integer> enumerable = Linq.asEnumerable(array);
        final Stream<Integer> stream = enumerable.stream();
        Assert.assertFalse(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Integer.class), Linq.asEnumerable(2, 4, 6));
    }

    @Test
    public void testStreamLong() {
        final long[] array = {2, 4, 6, 7};
        final IEnumerable<Long> enumerable = Linq.asEnumerable(array);
        final Stream<Long> stream = enumerable.stream();
        Assert.assertFalse(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Long.class), Linq.asEnumerable(2L, 4L, 6L));
    }

    @Test
    public void testStreamDouble() {
        final double[] array = {2d, 4d, 6d, 7d};
        final IEnumerable<Double> enumerable = Linq.asEnumerable(array);
        final Stream<Double> stream = enumerable.stream();
        Assert.assertFalse(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Double.class), Linq.asEnumerable(2d, 4d, 6d));
    }

    @Test
    public void testStream2() {
        final Stream<Integer> stream = Linq.range(0, 2048).stream(false);
        Assert.assertFalse(stream.isParallel());
        final Object[] objects = stream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Integer.class), Linq.range(1024, 1024));
        //
        final Stream<Integer> pStream = Linq.range(0, 2048).stream(true);
        Assert.assertTrue(pStream.isParallel());
        final Object[] pObjects = pStream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.asEnumerable(pObjects).cast(Integer.class), Linq.range(1024, 1024));
    }

    @Test
    public void testStreamInt2() {
        final int[] array = {2, 4, 6, 7};
        final IEnumerable<Integer> enumerable = Linq.asEnumerable(array);
        final Stream<Integer> stream = enumerable.stream(false);
        Assert.assertFalse(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Integer.class), Linq.asEnumerable(2, 4, 6));
        //
        final Stream<Integer> pStream = enumerable.stream(true);
        Assert.assertTrue(pStream.isParallel());
        final Object[] PObjects = pStream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.asEnumerable(PObjects).cast(Integer.class), Linq.asEnumerable(2, 4, 6));
    }

    @Test
    public void testStreamLong2() {
        final long[] array = {2, 4, 6, 7};
        final IEnumerable<Long> enumerable = Linq.asEnumerable(array);
        final Stream<Long> stream = enumerable.stream(false);
        Assert.assertFalse(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Long.class), Linq.asEnumerable(2L, 4L, 6L));
        //
        final Stream<Long> pStream = enumerable.stream(true);
        Assert.assertTrue(pStream.isParallel());
        final Object[] pObjects = pStream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.asEnumerable(pObjects).cast(Long.class), Linq.asEnumerable(2L, 4L, 6L));
    }

    @Test
    public void testStreamDouble2() {
        final double[] array = {2d, 4d, 6d, 7d};
        final IEnumerable<Double> enumerable = Linq.asEnumerable(array);
        final Stream<Double> stream = enumerable.stream(false);
        Assert.assertFalse(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Double.class), Linq.asEnumerable(2d, 4d, 6d));
        //
        final Stream<Double> pStream = enumerable.stream(true);
        Assert.assertTrue(pStream.isParallel());
        final Object[] pObjects = pStream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.asEnumerable(pObjects).cast(Double.class), Linq.asEnumerable(2d, 4d, 6d));
    }

    @Test
    public void testStream3() {
        final Stream<Integer> stream = Linq.range(0, 2048).parallelStream();
        Assert.assertTrue(stream.isParallel());
        final Object[] objects = stream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Integer.class), Linq.range(1024, 1024));
    }

    @Test
    public void testStreamInt3() {
        final int[] array = {2, 4, 6, 7};
        final IEnumerable<Integer> enumerable = Linq.asEnumerable(array);
        final Stream<Integer> stream = enumerable.parallelStream();
        Assert.assertTrue(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Integer.class), Linq.asEnumerable(2, 4, 6));
    }

    @Test
    public void testStreamLong3() {
        final long[] array = {2, 4, 6, 7};
        final IEnumerable<Long> enumerable = Linq.asEnumerable(array);
        final Stream<Long> stream = enumerable.parallelStream();
        Assert.assertTrue(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Long.class), Linq.asEnumerable(2L, 4L, 6L));
    }

    @Test
    public void testStreamDouble3() {
        final double[] array = {2d, 4d, 6d, 7d};
        final IEnumerable<Double> enumerable = Linq.asEnumerable(array);
        final Stream<Double> stream = enumerable.parallelStream();
        Assert.assertTrue(stream.isParallel());
        final Object[] objects = stream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.asEnumerable(objects).cast(Double.class), Linq.asEnumerable(2d, 4d, 6d));
    }
}
