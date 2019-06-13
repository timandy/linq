package com.bestvike.linq;

import com.bestvike.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by 许崇雷 on 2017-07-24.
 */
public class LinqTest extends TestCase {
    @Test
    public void testBoolean() {
        boolean[] array = {true, false, true};
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        list.add(false);
        list.add(true);
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testByte() {
        byte[] array = {0x01, 0x02, 0x03};
        List<Byte> list = new ArrayList<>();
        list.add(Byte.valueOf("01"));
        list.add(Byte.valueOf("02"));
        list.add(Byte.valueOf("03"));
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testShort() {
        short[] array = {0x01, 0x02, 0x03};
        List<Short> list = new ArrayList<>();
        list.add(Short.valueOf("01"));
        list.add(Short.valueOf("02"));
        list.add(Short.valueOf("03"));
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testInt() {
        int[] array = {0x01, 0x02, 0x03};
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testLong() {
        long[] array = {0x01, 0x02, 0x03};
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testFloat() {
        float[] array = {1f, 2f, Float.NaN};
        List<Float> list = new ArrayList<>();
        list.add(1f);
        list.add(2f);
        list.add(Float.NaN);
        assertEquals(true, Objects.equals(Float.NaN, Float.NaN));
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testDouble() {
        double[] array = {1d, 2d, Double.NaN};
        List<Double> list = new ArrayList<>();
        list.add(1d);
        list.add(2d);
        list.add(Double.NaN);
        assertEquals(true, Objects.equals(Double.NaN, Double.NaN));
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testChar() {
        char[] array = {'a', 'b', 'c'};
        List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        list.add('c');
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testArrayAndList() {
        String[] array = {"1", "2", "3"};
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        assertEquals(3, Linq.asEnumerable(array).count());
        assertEquals(3, Linq.asEnumerable(list).count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testCollectionAndIterable() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        set.add(2L);
        set.add(3L);
        Iterable<Long> iterableDemo = new CountIterable(3);
        assertEquals(3, Linq.asEnumerable(set).count());
        assertEquals(3, Linq.asEnumerable(iterableDemo).count());
        assertTrue(Linq.asEnumerable(set).sequenceEqual(Linq.asEnumerable(iterableDemo)));
    }

    @Test
    public void testCharSequence() {
        String str = "123";
        char[] chars = {'1', '2', '3'};
        char c = Linq.asEnumerable(str).elementAt(1);
        assertEquals('2', c);
        assertEquals(3, Linq.asEnumerable(str).count());
        assertEquals(3, Linq.asEnumerable(chars).count());
        assertTrue(Linq.asEnumerable(str).sequenceEqual(Linq.asEnumerable(str)));
    }

    @Test
    public void testMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        IEnumerable<Map.Entry<Integer, String>> enu1 = Linq.asEnumerable(map);

        Map<Integer, String> map2 = new HashMap<>();
        map2.put(1, "1");
        map2.put(2, "2");
        map2.put(3, "3");
        IEnumerable<Map.Entry<Integer, String>> enu2 = Linq.asEnumerable(map2);
        map2.put(4, "4");
        map2.remove(4);

        assertEquals(3, Linq.asEnumerable(enu1).count());
        assertEquals(3, Linq.asEnumerable(enu2).count());
    }

    @Test
    public void testSingleton() {
        String[] array = {"1"};
        assertEquals(1, Linq.asEnumerable(array).count());
        assertEquals(1, Linq.singleton("1").count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.singleton("1")));
    }

    @Test
    public void testRange() {
        Integer[] array = {1, 2, 3, 4, 5};
        IEnumerable<Integer> integers = Linq.range(1, 5);
        assertEquals(5, integers.count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(integers));
    }

    @Test
    public void testRepeat() {
        Integer[] array = {1, 1, 1, 1, 1};
        IEnumerable<Integer> integers = Linq.repeat(1, 5);
        assertEquals(5, integers.count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(integers));
    }

    @Test
    public void testEmpty() {
        Integer[] array = {};
        IEnumerable<Integer> integers = Linq.empty();
        assertEquals(0, integers.count());
        assertTrue(Linq.asEnumerable(array).sequenceEqual(integers));
    }
}
