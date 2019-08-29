package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by 许崇雷 on 2019-04-25.
 */
class ToSpliteratorTest extends TestCase {
    @Test
    void testReset() {
        IEnumerator<Employee> enumerator = Linq.of(emps).enumerator();
        assertThrows(NotSupportedException.class, enumerator::reset);
    }

    @Test
    void testRemove() {
        IEnumerator<Employee> enumerator = Linq.of(emps).enumerator();
        enumerator.moveNext();
        assertThrows(NotSupportedException.class, enumerator::remove);
    }

    @Test
    void testForEachRemaining() {
        List<String> ids = new ArrayList<>();
        try (IEnumerator<Employee> e = Linq.of(emps).enumerator()) {
            e.moveNext();
            e.moveNext();
            e.forEachRemaining(a -> ids.add(String.valueOf(a.empno)));
        }
        assertEquals(2, ids.size());
        assertEquals("120", ids.get(0));
        assertEquals("130", ids.get(1));
    }

    @Test
    void testForEach() {
        List<String> ids = new ArrayList<>();
        IEnumerable<Employee> enumerable = Linq.of(emps);
        enumerable.forEach(a -> ids.add(String.valueOf(a.empno)));
        assertThrows(ArgumentNullException.class, () -> enumerable.forEach(null));
        assertEquals(4, ids.size());
        assertEquals("100", ids.get(0));
        assertEquals("110", ids.get(1));
        assertEquals("120", ids.get(2));
        assertEquals("130", ids.get(3));
    }

    @Test
    void testStream() {
        Stream<Integer> stream = Linq.range(0, 2048).stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.range(1024, 1024));
    }

    @Test
    void testStreamBoolean() {
        boolean[] array = {false, false, false, false};
        IEnumerable<Boolean> enumerable = Linq.of(array);
        Stream<Boolean> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a).toArray();
        assertEquals(Linq.of(objects).cast(Boolean.class), Linq.empty());
    }

    @Test
    void testStreamByte() {
        byte[] array = {1, 2, 3, 4};
        IEnumerable<Byte> enumerable = Linq.of(array);
        Stream<Byte> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a == 5).toArray();
        assertEquals(Linq.of(objects).cast(Byte.class), Linq.empty());
    }

    @Test
    void testStreamShort() {
        short[] array = {1, 2, 3, 4};
        IEnumerable<Short> enumerable = Linq.of(array);
        Stream<Short> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a == 5).toArray();
        assertEquals(Linq.of(objects).cast(Short.class), Linq.empty());
    }

    @Test
    void testStreamInt() {
        int[] array = {2, 4, 6, 7};
        IEnumerable<Integer> enumerable = Linq.of(array);
        Stream<Integer> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.of(2, 4, 6));
    }

    @Test
    void testStreamLong() {
        long[] array = {2, 4, 6, 7};
        IEnumerable<Long> enumerable = Linq.of(array);
        Stream<Long> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Long.class), Linq.of(2L, 4L, 6L));
    }

    @Test
    void testStreamChar() {
        char[] array = {'a', 'b', 'c', 'd'};
        IEnumerable<Character> enumerable = Linq.of(array);
        Stream<Character> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a == 'e').toArray();
        assertEquals(Linq.of(objects).cast(Character.class), Linq.empty());
    }

    @Test
    void testStreamFloat() {
        float[] array = {2f, 4f, 6f, 7f};
        IEnumerable<Float> enumerable = Linq.of(array);
        Stream<Float> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0f).toArray();
        assertEquals(Linq.of(objects).cast(Float.class), Linq.of(2f, 4f, 6f));
    }

    @Test
    void testStreamDouble() {
        double[] array = {2d, 4d, 6d, 7d};
        IEnumerable<Double> enumerable = Linq.of(array);
        Stream<Double> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.of(objects).cast(Double.class), Linq.of(2d, 4d, 6d));
    }

    @Test
    void testStreamCharSequence() {
        IEnumerable<Character> enumerable = Linq.chars("abcd");
        Stream<Character> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a == 'e').toArray();
        assertEquals(Linq.of(objects).cast(Character.class), Linq.empty());
    }

    @Test
    void testStreamGenericArray() {
        Integer[] array = {2, 4, 6, 7};
        IEnumerable<Integer> enumerable = Linq.of(array);
        Stream<Integer> stream = enumerable.stream();
        assertFalse(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.of(2, 4, 6));
    }

    @Test
    void testStream2() {
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
    void testStreamInt2() {
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
    void testStreamLong2() {
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
    void testStreamDouble2() {
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
    void testStream3() {
        Stream<Integer> stream = Linq.range(0, 2048).parallelStream();
        assertTrue(stream.isParallel());
        Object[] objects = stream.filter(a -> a >= 1024).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.range(1024, 1024));
    }

    @Test
    void testStreamInt3() {
        int[] array = {2, 4, 6, 7};
        IEnumerable<Integer> enumerable = Linq.of(array);
        Stream<Integer> stream = enumerable.parallelStream();
        assertTrue(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Integer.class), Linq.of(2, 4, 6));
    }

    @Test
    void testStreamLong3() {
        long[] array = {2, 4, 6, 7};
        IEnumerable<Long> enumerable = Linq.of(array);
        Stream<Long> stream = enumerable.parallelStream();
        assertTrue(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0).toArray();
        assertEquals(Linq.of(objects).cast(Long.class), Linq.of(2L, 4L, 6L));
    }

    @Test
    void testStreamDouble3() {
        double[] array = {2d, 4d, 6d, 7d};
        IEnumerable<Double> enumerable = Linq.of(array);
        Stream<Double> stream = enumerable.parallelStream();
        assertTrue(stream.isParallel());
        Object[] objects = stream.filter(a -> a % 2 == 0d).toArray();
        assertEquals(Linq.of(objects).cast(Double.class), Linq.of(2d, 4d, 6d));
    }

    @Test
    void testStreamGrouping() {
        int[] element = {60, -10, 40, 100};
        Tuple2[] source = new Tuple2[]{
                Tuple.create("Tim", element[0]),
                Tuple.create("Tim", element[1]),
                Tuple.create("miT", element[2]),
                Tuple.create("miT", element[3])
        };

        assertEquals(Linq.of(60, -10), Linq.of(Linq.of(source).toLookup(Tuple2::getItem1, Tuple2::getItem2).get("Tim").stream().toArray()));
    }
}
