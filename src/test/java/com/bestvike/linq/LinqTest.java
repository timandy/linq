package com.bestvike.linq;

import com.bestvike.TestCase;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.linq.util.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

/**
 * Created by 许崇雷 on 2017-07-24.
 */
public class LinqTest extends TestCase {
    @Test
    public void testEmpty() {
        Integer[] array = {};
        IEnumerable<Integer> integers = Linq.empty();
        assertEquals(0, integers.count());
        assertTrue(Linq.of(array).sequenceEqual(integers));
    }

    @Test
    public void testSingleton() {
        String[] array = {"1"};
        assertEquals(1, Linq.of(array).count());
        assertEquals(1, Linq.singleton("1").count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.singleton("1")));
    }

    @Test
    public void testBoolean() {
        boolean[] array = {true, false, true};
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        list.add(false);
        list.add(true);
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testByte() {
        byte[] array = {0x01, 0x02, 0x03};
        List<Byte> list = new ArrayList<>();
        list.add(Byte.valueOf("01"));
        list.add(Byte.valueOf("02"));
        list.add(Byte.valueOf("03"));
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testShort() {
        short[] array = {0x01, 0x02, 0x03};
        List<Short> list = new ArrayList<>();
        list.add(Short.valueOf("01"));
        list.add(Short.valueOf("02"));
        list.add(Short.valueOf("03"));
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testInt() {
        int[] array = {0x01, 0x02, 0x03};
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testLong() {
        long[] array = {0x01, 0x02, 0x03};
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testFloat() {
        float[] array = {1f, 2f, Float.NaN};
        List<Float> list = new ArrayList<>();
        list.add(1f);
        list.add(2f);
        list.add(Float.NaN);
        assertEquals(true, Objects.equals(Float.NaN, Float.NaN));
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testDouble() {
        double[] array = {1d, 2d, Double.NaN};
        List<Double> list = new ArrayList<>();
        list.add(1d);
        list.add(2d);
        list.add(Double.NaN);
        assertEquals(true, Objects.equals(Double.NaN, Double.NaN));
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testChar() {
        char[] array = {'a', 'b', 'c'};
        List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        list.add('c');
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testArrayAndList() {
        String[] array = {"1", "2", "3"};
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        assertEquals(3, Linq.of(array).count());
        assertEquals(3, Linq.of(list).count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.of(list)));
    }

    @Test
    public void testCollectionAndIterable() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        set.add(2L);
        set.add(3L);
        Iterable<Long> iterableDemo = new CountIterable(3);
        assertEquals(3, Linq.of(set).count());
        assertEquals(3, Linq.of(iterableDemo).count());
        assertTrue(Linq.of(set).sequenceEqual(Linq.of(iterableDemo)));
    }

    @Test
    public void testEnumerable() {
        IEnumerable<Integer> enumerable = Linq.repeat(1, 5);
        IEnumerable<Integer> source = Linq.of(enumerable);
        assertSame(enumerable, source);

        assertThrows(ArgumentNullException.class, () -> Linq.of((IEnumerable<?>) null));
    }

    @Test
    public void testIterator() {
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        IEnumerable<Integer> source = Linq.of(iterator);
        assertEquals(Linq.range(1, 3), source);
        assertThrows(NotSupportedException.class, () -> source.enumerator());

        assertThrows(ArgumentNullException.class, () -> Linq.of((Iterator<?>) null));
    }

    @Test
    public void testEnumeration() {
        Enumeration<Integer> elements = new Vector<>(Arrays.asList(1, 2, 3)).elements();
        IEnumerable<Integer> source = Linq.of(elements);
        assertEquals(Linq.range(1, 3), source);
        assertThrows(NotSupportedException.class, () -> source.enumerator());

        assertThrows(ArgumentNullException.class, () -> Linq.of((Enumeration<?>) null));

        //append,prepend,concat
        IEnumerable<Integer> source2 = Linq.of(new Vector<>(Arrays.asList(1, 2, 3)).elements()).prepend(0).append(4).append(5).concat(Linq.range(6, 5));
        assertEquals(Linq.range(0, 11), source2);
        assertThrows(NotSupportedException.class, () -> source2.toArray());
    }

    @Test
    public void testMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        IEnumerable<Map.Entry<Integer, String>> enu1 = Linq.of(map);

        Map<Integer, String> map2 = new HashMap<>();
        map2.put(1, "1");
        map2.put(2, "2");
        map2.put(3, "3");
        IEnumerable<Map.Entry<Integer, String>> enu2 = Linq.of(map2);
        map2.put(4, "4");
        map2.remove(4);

        assertEquals(3, Linq.of(enu1).count());
        assertEquals(3, Linq.of(enu2).count());
    }

    @Test
    public void testObject() {
        assertNull(Linq.as(null));
        assertNull(Linq.as(new Object()));

        boolean[] booleans = {true, false, true};
        assertEquals(Linq.of(booleans), Linq.as(booleans));

        byte[] bytes = {0x01, 0x02, 0x03};
        assertEquals(Linq.of(bytes), Linq.as(bytes));

        short[] shorts = {0x01, 0x02, 0x03};
        assertEquals(Linq.of(shorts), Linq.as(shorts));

        int[] ints = {0x01, 0x02, 0x03};
        assertEquals(Linq.of(ints), Linq.as(ints));

        long[] longs = {0x01, 0x02, 0x03};
        assertEquals(Linq.of(longs), Linq.as(longs));

        float[] floats = {1f, 2f, Float.NaN};
        assertEquals(Linq.of(floats), Linq.as(floats));

        double[] doubles = {1d, 2d, Double.NaN};
        assertEquals(Linq.of(doubles), Linq.as(doubles));

        char[] chars = {'a', 'b', 'c'};
        assertEquals(Linq.of(chars), Linq.as(chars));

        //CharSequence
        String str = "123";
        assertNull(Linq.as(str));

        //Array
        String[] strings = {"1", "2", "3"};
        assertEquals(Linq.of(strings), Linq.as(strings));

        //IEnumerable
        IEnumerable<String> enumerable = Linq.of(strings);
        assertSame(enumerable, Linq.as(enumerable));

        //List
        assertEquals(Linq.of(strings), Linq.as(Arrays.asList(strings)));

        //Collection
        assertEquals(Linq.of(strings), Linq.as(ArrayUtils.toCollection(strings)));

        //Iterable
        assertEquals(Linq.of(1L, 2L, 3L), Linq.as(new CountIterable(3)));

        //Iterator
        assertEquals(Linq.of(1L, 2L, 3L), Linq.as(new CountIterator(3)));

        //Enumeration
        assertEquals(Linq.range(1, 3), Linq.as(new Vector<>(Arrays.asList(1, 2, 3)).elements()));

        //Map
        Map<String, Integer> map = new HashMap<>();
        map.put("hello", 1);
        map.put("world", 2);
        IEnumerable<Map.Entry<String, Integer>> entries = Linq.as(map);
        assertEquals(2, entries.count());
        assertEquals(2, entries.where(a -> a.getKey().equals("world")).first().getValue());
    }

    @Test
    public void testOfNullable() {
        assertSame(Linq.empty(), Linq.ofNullable(null));
        assertEquals(Linq.of("hello"), Linq.ofNullable("hello"));
    }

    @Test
    public void testChars() {
        String str = "123";
        char[] chars = {'1', '2', '3'};
        char c = Linq.chars(str).elementAt(1);
        assertEquals('2', c);
        assertEquals(3, Linq.chars(str).count());
        assertEquals(3, Linq.of(chars).count());
        assertTrue(Linq.chars(str).sequenceEqual(Linq.chars(str)));
    }

    @Test
    public void testWords() {
        assertThrows(ArgumentNullException.class, () -> Linq.words(null));
        assertEquals(Linq.empty(), Linq.words(""));
        assertEquals(Linq.empty(), Linq.words("\r"));
        assertEquals(Linq.empty(), Linq.words("\r\n\n"));
        assertEquals(Linq.of("hello"), Linq.words("hello"));
        assertEquals(Linq.of("hello", "world"), Linq.words("hello\r\n\n\nworld"));
        assertEquals(Linq.of("hello", "world"), Linq.words("\r\n\n\nhello\r\n\n\nworld"));
        assertEquals(Linq.of("hello", "world"), Linq.words("hello\r\n\n\nworld\r\n\n\n"));
        assertEquals(Linq.of("hello", "world"), Linq.words("\r\n\n\nhello\r\n\n\nworld\r\n\n\n"));
        assertEquals(Linq.of("hello", "world"), Linq.words("\r\n\n\nhello\r\n\n\nworld\r\n\n\n  "));

        assertEquals(Linq.of("hello", "world"), Linq.words("hello world!"));
        assertEquals(Linq.of("hello", "world"), Linq.words(" hello world!"));
        assertEquals(Linq.of("hello", "world"), Linq.words(" hello world! "));
        assertEquals(Linq.of("hello", "world"), Linq.words(" hello,world! "));
        assertEquals(Linq.of("p2", "is", "the", "second", "parameter"), Linq.words(" p2 is the second parameter. "));
    }

    @Test
    public void testLines() {
        assertThrows(ArgumentNullException.class, () -> Linq.lines(null));
        assertEquals(Linq.empty(), Linq.lines(""));
        assertEquals(Linq.empty(), Linq.lines("\r"));
        assertEquals(Linq.empty(), Linq.lines("\r\n\n"));
        assertEquals(Linq.of("hello"), Linq.lines("hello"));
        assertEquals(Linq.of("hello", "world"), Linq.lines("hello\r\n\n\nworld"));
        assertEquals(Linq.of("hello", "world"), Linq.lines("\r\n\n\nhello\r\n\n\nworld"));
        assertEquals(Linq.of("hello", "world"), Linq.lines("hello\r\n\n\nworld\r\n\n\n"));
        assertEquals(Linq.of("hello", "world"), Linq.lines("\r\n\n\nhello\r\n\n\nworld\r\n\n\n"));
        assertEquals(Linq.of("hello", "world", "  "), Linq.lines("\r\n\n\nhello\r\n\n\nworld\r\n\n\n  "));
    }

    @Test
    public void testEnumerate() {
        IEnumerable<Integer> source = Linq.enumerate(0, x -> x + 1);
        assertEquals(0, source.first());
        assertEquals(0, source.runOnce().first());
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(0, source.elementAt(0));
        assertEquals(5, source.elementAt(5));
        assertThrows(ArithmeticException.class, () -> source.count());
    }

    @Test
    public void testEnumerate2() {
        IEnumerable<Integer> source = Linq.enumerate(0, x -> x < 100, x -> x + 1);
        assertEquals(0, source.first());
        assertEquals(0, source.runOnce().first());
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(0, source.elementAt(0));
        assertEquals(5, source.elementAt(5));
        assertEquals(100, source.count());

        IEnumerable<Integer> source2 = Linq.enumerate(100, x -> x < 0, x -> --x);
        assertThrows(InvalidOperationException.class, () -> source2.first());
        assertThrows(ArgumentOutOfRangeException.class, () -> source2.elementAt(0));
        assertEquals(0, source2.count());
    }

    @Test
    public void testGenerate() {
        IEnumerable<Integer> source = Linq.generate(() -> 99);
        assertEquals(99, source.first());
        assertEquals(99, source.runOnce().first());
        assertEquals(99, source.elementAt(5));
        assertThrows(ArithmeticException.class, () -> source.count());
    }

    @Test
    public void testRange() {
        Integer[] array = {1, 2, 3, 4, 5};
        IEnumerable<Integer> integers = Linq.range(1, 5);
        assertEquals(5, integers.count());
        assertTrue(Linq.of(array).sequenceEqual(integers));
    }

    @Test
    public void testRepeat() {
        Integer[] array = {1, 1, 1, 1, 1};
        IEnumerable<Integer> integers = Linq.repeat(1, 5);
        assertEquals(5, integers.count());
        assertTrue(Linq.of(array).sequenceEqual(integers));
    }
}
