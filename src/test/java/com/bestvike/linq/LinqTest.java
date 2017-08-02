package com.bestvike.linq;

import com.bestvike.linq.entity.IterableDemo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by 许崇雷 on 2017/7/24.
 */
public class LinqTest {
    @Test
    public void testBoolean() {
        final boolean[] array = {true, false, true};
        final List<Boolean> list = new ArrayList<>();
        list.add(true);
        list.add(false);
        list.add(true);
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testByte() {
        final byte[] array = {0x01, 0x02, 0x03};
        final List<Byte> list = new ArrayList<>();
        list.add(Byte.valueOf("01"));
        list.add(Byte.valueOf("02"));
        list.add(Byte.valueOf("03"));
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testShort() {
        final short[] array = {0x01, 0x02, 0x03};
        final List<Short> list = new ArrayList<>();
        list.add(Short.valueOf("01"));
        list.add(Short.valueOf("02"));
        list.add(Short.valueOf("03"));
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testInt() {
        final int[] array = {0x01, 0x02, 0x03};
        final List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testLong() {
        final long[] array = {0x01, 0x02, 0x03};
        final List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testFloat() {
        final float[] array = {1f, 2f, Float.NaN};
        final List<Float> list = new ArrayList<>();
        list.add(1f);
        list.add(2f);
        list.add(Float.NaN);
        Assert.assertEquals(true, Objects.equals(Float.NaN, Float.NaN));
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testDouble() {
        final double[] array = {1d, 2d, Double.NaN};
        final List<Double> list = new ArrayList<>();
        list.add(1d);
        list.add(2d);
        list.add(Double.NaN);
        Assert.assertEquals(true, Objects.equals(Double.NaN, Double.NaN));
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testChar() {
        final char[] array = {'a', 'b', 'c'};
        final List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        list.add('c');
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testArrayAndList() {
        final String[] array = {"1", "2", "3"};
        final List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        Assert.assertEquals(3, Linq.asEnumerable(array).count());
        Assert.assertEquals(3, Linq.asEnumerable(list).count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.asEnumerable(list)));
    }

    @Test
    public void testCollectionAndIterable() {
        final Set<Long> set = new HashSet<>();
        set.add(1L);
        set.add(2L);
        set.add(3L);
        final Iterable<Long> iterableDemo = new IterableDemo(3);
        Assert.assertEquals(3, Linq.asEnumerable(set).count());
        Assert.assertEquals(3, Linq.asEnumerable(iterableDemo).count());
        Assert.assertTrue(Linq.asEnumerable(set).sequenceEqual(Linq.asEnumerable(iterableDemo)));
    }

    @Test
    public void testCharSequence() {
        final String str = "123";
        final char[] chars = {'1', '2', '3'};
        char c = Linq.asEnumerable(str).elementAt(1);
        Assert.assertEquals('2', c);
        Assert.assertEquals(3, Linq.asEnumerable(str).count());
        Assert.assertEquals(3, Linq.asEnumerable(chars).count());
        Assert.assertTrue(Linq.asEnumerable(str).sequenceEqual(Linq.asEnumerable(str)));
    }

    @Test
    public void testMap() {
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        IEnumerable<Map.Entry<Integer, String>> enu1 = Linq.asEnumerable(map);

        final Map<Integer, String> map2 = new HashMap<>();
        map2.put(1, "1");
        map2.put(2, "2");
        map2.put(3, "3");
        IEnumerable<Map.Entry<Integer, String>> enu2 = Linq.asEnumerable(map2);
        map2.put(4, "4");
        map2.remove(4);

        Assert.assertEquals(3, Linq.asEnumerable(enu1).count());
        Assert.assertEquals(3, Linq.asEnumerable(enu2).count());
    }

    @Test
    public void testSingleton() {
        final String[] array = {"1"};
        Assert.assertEquals(1, Linq.asEnumerable(array).count());
        Assert.assertEquals(1, Linq.singletonEnumerable("1").count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(Linq.singletonEnumerable("1")));
    }

    @Test
    public void testRange() {
        final Integer[] array = {1, 2, 3, 4, 5};
        final IEnumerable<Integer> integers = Linq.range(1, 5);
        Assert.assertEquals(5, integers.count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(integers));
    }

    @Test
    public void testRepeat() {
        final Integer[] array = {1, 1, 1, 1, 1};
        final IEnumerable<Integer> integers = Linq.repeat(1, 5);
        Assert.assertEquals(5, integers.count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(integers));
    }

    @Test
    public void testEmpty() {
        final Integer[] array = {};
        final IEnumerable<Integer> integers = Linq.empty();
        Assert.assertEquals(0, integers.count());
        Assert.assertTrue(Linq.asEnumerable(array).sequenceEqual(integers));
    }
}
