package com.bestvike.linq;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.linq.enumerable.AbstractIterator;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.linq.exception.RepeatInvokeException;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.Strings;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Vector;
import java.util.stream.Stream;

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

        try (IEnumerator<Integer> e = Linq.<Integer>empty().enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testSingleton() {
        String[] array = {"1"};
        assertEquals(1, Linq.of(array).count());
        assertEquals(1, Linq.singleton("1").count());
        assertTrue(Linq.of(array).sequenceEqual(Linq.singleton("1")));

        try (IEnumerator<Integer> e = Linq.singleton(1).enumerator()) {
            assertTrue(e.moveNext());
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testOfNullable() {
        assertSame(Linq.empty(), Linq.ofNullable(null));
        assertEquals(Linq.of("hello"), Linq.ofNullable("hello"));

        Integer item = null;
        try (IEnumerator<Integer> e = Linq.ofNullable(item).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        try (IEnumerator<Integer> e = Linq.ofNullable(1).enumerator()) {
            assertTrue(e.moveNext());
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
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

        try (IEnumerator<Boolean> e = Linq.of(new boolean[0]).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((boolean[]) null));
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

        try (IEnumerator<Byte> e = Linq.of(new byte[0]).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((byte[]) null));
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

        try (IEnumerator<Short> e = Linq.of(new short[0]).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((short[]) null));
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

        try (IEnumerator<Integer> e = Linq.of(new int[0]).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((int[]) null));
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

        try (IEnumerator<Long> e = Linq.of(new long[0]).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((long[]) null));
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

        try (IEnumerator<Float> e = Linq.of(new float[0]).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((float[]) null));
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

        try (IEnumerator<Double> e = Linq.of(new double[0]).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((double[]) null));
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

        try (IEnumerator<Character> e = Linq.of(new char[0]).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((char[]) null));
    }

    @Test
    public void testArray() {
        Integer[] array = {1, 2, 3};
        assertEquals(3, Linq.of(array).count());
        assertEquals(Linq.of(1, 2, 3), Linq.of(array));

        try (IEnumerator<Integer> e = Linq.<Integer>of().enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Integer[]) null));
    }

    @Test
    public void testArrayInLinq() {
        Array<Integer> array = new Array<>(new Object[]{1, 2, 3});
        assertEquals(3, Linq.of(array).count());
        assertEquals(Linq.of(1, 2, 3), Linq.of(array));

        try (IEnumerator<Integer> e = Linq.of(new Array<Integer>(new Object[0])).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Array<Integer>) null));
    }

    @Test
    public void testArrayList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, Linq.of(list).count());
        assertEquals(Linq.of(1, 2, 3), Linq.of(list));

        try (IEnumerator<Integer> e = Linq.of(new ArrayList<Integer>()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((ArrayList<?>) null));
    }

    @Test
    public void testLinkedList() {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, Linq.of(list).count());
        assertEquals(Linq.of(1, 2, 3), Linq.of(list));

        try (IEnumerator<Integer> e = Linq.of(new LinkedList<Integer>()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((LinkedList<?>) null));
    }

    @Test
    public void testCollection() {
        Collection<Integer> collection = new HashSet<>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        assertEquals(3, Linq.of(collection).count());
        assertEquals(Linq.of(1, 2, 3), Linq.of(collection));

        try (IEnumerator<Long> e = Linq.of(new HashSet<Long>()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Collection<?>) null));
    }

    @Test
    public void testEnumerable() {
        IEnumerable<Integer> enumerable = new TestEnumerable<>(new Integer[]{1, 2, 3});
        IEnumerable<Integer> source = Linq.of(enumerable);
        assertSame(enumerable, source);

        try (IEnumerator<Integer> e = Linq.of(new TestEnumerable<>(new Integer[0])).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((IEnumerable<?>) null));
    }

    @Test
    public void testEnumerator() {
        IEnumerator<Integer> enumerator = new TestEnumerable<>(new Integer[]{1, 2, 3}).enumerator();
        IEnumerable<Integer> source = Linq.of(enumerator);
        assertSame(enumerator, source.enumerator());

        assertThrows(RepeatInvokeException.class, () -> source.enumerator());
        assertThrows(ArgumentNullException.class, () -> Linq.of((IEnumerator<?>) null));
    }

    @Test
    public void testIterable() {
        Iterable<Integer> list = new ArrayIterable<>(1, 2, 3);
        assertEquals(3, Linq.of(list).count());
        assertEquals(Linq.of(1, 2, 3), Linq.of(list));

        try (IEnumerator<Integer> e = Linq.of(new ArrayIterable<Integer>()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Iterable<?>) null));
    }

    @Test
    public void testIterator() {
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        IEnumerable<Integer> source = Linq.of(iterator);
        assertEquals(Linq.of(1, 2, 3), source);
        assertThrows(RepeatInvokeException.class, () -> source.enumerator());

        try (IEnumerator<Integer> e = Linq.of(Collections.<Integer>emptyIterator()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Iterator<?>) null));
    }

    @Test
    public void testStream() {
        IEnumerable<Integer> source = Linq.of(Stream.of(1, 2, 3));
        assertEquals(Linq.of(1, 2, 3), source);
        assertThrows(RepeatInvokeException.class, () -> source.enumerator());

        try (IEnumerator<Integer> e = Linq.of(Stream.<Integer>empty()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Stream<?>) null));
    }

    @Test
    public void testSpliterator() {
        IEnumerable<Integer> source = Linq.of(Stream.of(1, 2, 3).spliterator());
        assertEquals(Linq.of(1, 2, 3), source);
        assertThrows(RepeatInvokeException.class, () -> source.enumerator());

        try (IEnumerator<Integer> e = Linq.of(Stream.<Integer>empty().spliterator()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Spliterator<?>) null));
    }

    @Test
    public void testEnumeration() {
        Enumeration<Integer> elements = new Vector<>(Arrays.asList(1, 2, 3)).elements();
        IEnumerable<Integer> source = Linq.of(elements);
        assertEquals(Linq.of(1, 2, 3), source);
        assertThrows(RepeatInvokeException.class, () -> source.enumerator());

        try (IEnumerator<Integer> e = Linq.of(new Vector<Integer>().elements()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        try (IEnumerator<Integer> e = (IEnumerator<Integer>) Linq.of(new Vector<Integer>().elements()).toEnumeration()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Enumeration<?>) null));

        //append,prepend,concat
        IEnumerable<Integer> source2 = Linq.of(new Vector<>(Arrays.asList(1, 2, 3)).elements()).prepend(0).append(4).append(5).concat(Linq.range(6, 4));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), source2);
        assertThrows(RepeatInvokeException.class, () -> source2.toArray());
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

        try (IEnumerator<Map.Entry<Integer, String>> e = Linq.of(new HashMap<Integer, String>()).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.of((Map<?, ?>) null));
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

        //List
        assertEquals(Linq.of(strings), Linq.as(Arrays.asList(strings)));

        //Collection
        assertEquals(Linq.of(strings), Linq.as(ArrayUtils.toCollection(strings)));

        //IEnumerable
        IEnumerable<String> enumerable = Linq.of(strings);
        assertSame(enumerable, Linq.as(enumerable));

        //Iterable
        assertEquals(Linq.of(1L, 2L, 3L), Linq.as(new CountIterable(3)));

        //Stream
        IEnumerable<Object> streamEnumerable = Linq.as(Stream.of(1, 2, 3));
        assertEquals(Linq.of(1, 2, 3), streamEnumerable);
        assertThrows(RepeatInvokeException.class, () -> streamEnumerable.enumerator());

        //IEnumerator
        IEnumerator<String> enumerator = Linq.of(strings).enumerator();
        assertSame(enumerator, Linq.as(enumerator).enumerator());

        //Iterator
        assertEquals(Linq.of(1L, 2L, 3L), Linq.as(new CountIterator(3)));

        //Spliterator
        IEnumerable<Object> spliteratorEnumerable = Linq.as(Stream.of(1, 2, 3).spliterator());
        assertEquals(Linq.of(1, 2, 3), spliteratorEnumerable);
        assertThrows(RepeatInvokeException.class, () -> spliteratorEnumerable.enumerator());

        //Enumeration
        assertEquals(Linq.of(1, 2, 3), Linq.as(new Vector<>(Arrays.asList(1, 2, 3)).elements()));

        //Map
        Map<String, Integer> map = new HashMap<>();
        map.put("hello", 1);
        map.put("world", 2);
        IEnumerable<Map.Entry<String, Integer>> entries = Linq.as(map);
        assertEquals(2, entries.count());
        assertEquals(2, entries.where(a -> a.getKey().equals("world")).first().getValue());
    }

    @Test
    public void testChars() {
        assertThrows(ArgumentNullException.class, () -> Linq.chars(null));

        IEnumerable<Character> source = Linq.chars("123");
        char c = source.elementAt(1);
        assertEquals('2', c);
        assertEquals(3, source.count());
        assertTrue(Linq.of('1', '2', '3').sequenceEqual(source));

        try (IEnumerator<Character> e = Linq.chars(Empty).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
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

        try (IEnumerator<String> e = Linq.words(Empty).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
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
        assertEquals(Linq.of("hello", "world", "  "), Linq.lines("\n\r\r\rhello\n\r\r\rworld\n\r\r\r  "));

        try (IEnumerator<String> e = Linq.lines(Empty).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testInfinite() {
        IEnumerable<Integer> source = Linq.infinite(99);
        assertEquals(99, source.first());
        assertEquals(99, source.runOnce().first());
        assertEquals(99, source.elementAt(5));
        assertThrows(ArithmeticException.class, () -> source.count());

        Integer item = null;
        IEnumerable<Integer> source2 = Linq.infinite(item);
        assertNull(source2.first());
        assertNull(source2.runOnce().first());
        assertNull(source2.elementAt(5));
    }

    @Test
    public void testInfinite2() {
        IEnumerable<Integer> source = Linq.infinite(() -> 99);
        assertEquals(99, source.first());
        assertEquals(99, source.runOnce().first());
        assertEquals(99, source.elementAt(5));
        assertThrows(ArithmeticException.class, () -> source.count());

        assertThrows(ArgumentNullException.class, () -> Linq.infinite(null));
    }

    @Test
    public void testLoop() {
        IEnumerable<Integer> source = Linq.loop(0, x -> x + 1);
        assertEquals(0, source.first());
        assertEquals(0, source.runOnce().first());
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(0, source.elementAt(0));
        assertEquals(5, source.elementAt(5));
        assertThrows(ArithmeticException.class, () -> source.count());

        assertThrows(ArgumentNullException.class, () -> Linq.loop(0, null));
    }

    @Test
    public void testLoop2() {
        IEnumerable<Integer> source = Linq.loop(0, x -> x < 100, x -> x + 1);
        assertEquals(0, source.first());
        assertEquals(0, source.runOnce().first());
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(0, source.elementAt(0));
        assertEquals(5, source.elementAt(5));
        assertEquals(100, source.count());

        IEnumerable<Integer> source2 = Linq.loop(100, x -> x < 0, x -> --x);
        assertThrows(InvalidOperationException.class, () -> source2.first());
        assertThrows(ArgumentOutOfRangeException.class, () -> source2.elementAt(0));
        assertEquals(0, source2.count());

        try (IEnumerator<Integer> e = Linq.loop(0, x -> x < 0, x -> x + 1).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(ArgumentNullException.class, () -> Linq.loop(0, x -> x > 0, null));
        assertThrows(ArgumentNullException.class, () -> Linq.loop(0, null, x -> x));
    }

    @Test
    public void testEnumerate() {
        assertThrows(ArgumentNullException.class, () -> Linq.enumerate(null, () -> 1));
        assertThrows(ArgumentNullException.class, () -> Linq.enumerate(() -> true, null));

        IEnumerator<Integer> enumerator = new TestEnumerable<>(new Integer[]{1, 2}).enumerator();
        IEnumerable<Integer> source = Linq.enumerate(enumerator::moveNext, enumerator::current);
        try (IEnumerator<Integer> e = source.enumerator()) {
            assertTrue(e.moveNext());
            assertEquals(1, e.current());
            assertTrue(e.moveNext());
            assertEquals(2, e.current());
            assertEquals(2, e.current());
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(RepeatInvokeException.class, () -> source.enumerator());
    }

    @Test
    public void testIterate() {
        assertThrows(ArgumentNullException.class, () -> Linq.iterate(null, () -> 1));
        assertThrows(ArgumentNullException.class, () -> Linq.iterate(() -> true, null));

        Iterator<Integer> iterator = new ArrayIterable<>(new Integer[]{1, 2}).iterator();
        IEnumerable<Integer> source = Linq.iterate(iterator::hasNext, iterator::next);
        try (IEnumerator<Integer> e = source.enumerator()) {
            assertTrue(e.moveNext());
            assertEquals(1, e.current());
            assertTrue(e.moveNext());
            assertEquals(2, e.current());
            assertEquals(2, e.current());
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        assertThrows(RepeatInvokeException.class, () -> source.enumerator());

        //read stream by line
        ByteArrayInputStream inputStream = new ByteArrayInputStream(" hello \r\n world \r\n bye ".getBytes(StandardCharsets.UTF_8));
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
             IEnumerator<String> e = Linq.iterate(scanner::hasNextLine, scanner::nextLine).select(x -> x == null ? Strings.Empty : x.trim()).enumerator()) {
            e.moveNext();
            assertEquals("hello", e.current());
            e.moveNext();
            assertEquals("world", e.current());
            e.moveNext();
            assertEquals("bye", e.current());
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testRange() {
        Integer[] array = {1, 2, 3, 4, 5};
        IEnumerable<Integer> integers = Linq.range(1, 5);
        assertEquals(5, integers.count());
        assertTrue(Linq.of(array).sequenceEqual(integers));

        try (IEnumerator<Integer> e = Linq.range(1, 0).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testRepeat() {
        Integer[] array = {1, 1, 1, 1, 1};
        IEnumerable<Integer> integers = Linq.repeat(1, 5);
        assertEquals(5, integers.count());
        assertTrue(Linq.of(array).sequenceEqual(integers));

        try (IEnumerator<Integer> e = Linq.repeat(1, 0).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testReadInputStreamByLine() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("hello\r\nworld".getBytes(StandardCharsets.UTF_8));
        IEnumerable<String> enumerable = new InputStreamLineEnumerable(inputStream, StandardCharsets.UTF_8);
        try (IEnumerator<String> e = enumerable.enumerator()) {
            assertTrue(e.moveNext());
            assertEquals("hello", e.current());
            assertTrue(e.moveNext());
            assertEquals("world", e.current());
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());

            e.reset();

            assertTrue(e.moveNext());
            assertEquals("hello", e.current());
        }
        assertEquals(-1, inputStream.read());

        assertEquals(2, enumerable.count());
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testOther() {
        try (IEnumerator<Object> e = Linq.empty().enumerator()) {
            assertThrows(NoSuchElementException.class, e::next);
            assertThrows(NotSupportedException.class, e::reset);
            assertThrows(NotSupportedException.class, e::remove);
            assertThrows(ArgumentNullException.class, () -> e.forEachRemaining(null));
        }
    }


    static final class InputStreamLineEnumerable extends AbstractIterator<String> {
        private final InputStream inputStream;
        private final Charset charset;
        private Scanner scanner;

        InputStreamLineEnumerable(InputStream inputStream, Charset charset) {
            if (inputStream == null)
                throw new ArgumentNullException("inputStream");
            if (charset == null)
                throw new ArgumentNullException("charset");

            this.inputStream = inputStream;
            this.charset = charset;
        }

        @Override
        public AbstractIterator<String> clone() {
            try {
                this.inputStream.reset();
            } catch (Exception e) {
                ThrowHelper.throwRepeatInvokeException();
            }
            return new InputStreamLineEnumerable(this.inputStream, this.charset);
        }

        @Override
        public boolean moveNext() {
            switch (this.state) {
                case 1:
                    this.scanner = new Scanner(this.inputStream, this.charset.name());
                    this.state = 2;
                case 2:
                    if (this.scanner.hasNext()) {
                        this.current = this.scanner.next();
                        return true;
                    }
                    this.close();
                    return false;
                default:
                    return false;
            }
        }

        @Override
        public void reset() {
            try {
                this.inputStream.reset();
                this.state = 1;
            } catch (Exception e) {
                ThrowHelper.throwNotSupportedException();
            }
        }

        @Override
        public void close() {
            if (this.scanner != null) {
                this.scanner.close();
                this.scanner = null;
            }
            super.close();
        }
    }
}
