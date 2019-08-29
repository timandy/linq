package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class ReverseTest extends TestCase {
    @Test
    void InvalidArguments() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).reverse());
    }

    @Test
    void Reverse() {
        for (Object[] objects : this.ReverseData()) {
            this.Reverse((IEnumerable<?>) objects[0]);
        }
    }

    private <T> void Reverse(IEnumerable<T> source) {
        List<T> expectedList = source.toList();
        Collections.reverse(expectedList);
        IEnumerable<T> expected = Linq.of(expectedList);

        IEnumerable<T> actual = source.reverse();

        assertEquals(expected, actual);
        assertEquals(expected.count(), actual.count()); // Count may be optimized.
        assertEquals(expected, Linq.of(actual.toArray()));
        assertEquals(expected, Linq.of(actual.toList()));

        assertEquals(expected.firstOrDefault(), actual.firstOrDefault());
        assertEquals(expected.lastOrDefault(), actual.lastOrDefault());

        for (int i = 0; i < expected.count(); i++) {
            assertEquals(expected.elementAt(i), actual.elementAt(i));

            assertEquals(expected.skip(i), actual.skip(i));
            assertEquals(expected.take(i), actual.take(i));
        }

        assertEquals(null, actual.elementAtOrDefault(-1));
        assertEquals(null, actual.elementAtOrDefault(expected.count()));

        assertEquals(expected, actual.select(a -> a));
        assertEquals(expected, actual.where(a -> true));

        assertEquals(actual, actual); // Repeat the enumeration against itself.
    }

    @Test
    void RunOnce() {
        for (Object[] objects : this.ReverseData()) {
            this.RunOnce((IEnumerable<?>) objects[0]);
        }
    }

    private <T> void RunOnce(IEnumerable<T> source) {
        List<T> expectedList = source.toList();
        Collections.reverse(expectedList);
        IEnumerable<T> expected = Linq.of(expectedList);

        IEnumerable<T> actual = source.runOnce().reverse();

        assertEquals(expected, actual);
    }

    private IEnumerable<Object[]> ReverseData() {
        IEnumerable<IEnumerable<?>> integers = Linq.of(
                Linq.of(), // No elements.
                Linq.of(new int[]{1}), // One element.
                Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}), // Distinct elements.
                Linq.of(new int[]{-10, 0, 5, 0, 9, 100, 9}) // Some repeating elements.
        );

        return integers
                .select(collection -> new Object[]{collection})
                .concat(integers.select(c -> new Object[]{c.select(Object::toString)}));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).reverse();
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void testReverseCount() {
        assertTrue(Linq.of(1, 2, 3).reverse().any());
        assertTrue(Linq.of(1, 2, 3).select(x -> x).reverse().any());
        assertTrue(new TestEnumerable<>(new Integer[]{1, 2, 3}).reverse().any());

        assertFalse(Linq.of().reverse().any());
        assertFalse(Linq.of().select(x -> x).reverse().any());
        assertFalse(new TestEnumerable<>(new Integer[0]).reverse().any());
    }

    @Test
    void testReverse() {
        //null 在前,值相等的按原始顺序
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .orderBy(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        //reverse 与 orderByDescending 不同.reverse 是完全反序,orderByDescending  如果相等保持原始顺序
        String ss = Linq.of(emps).concat(Linq.of(badEmps))
                .orderBy(emp -> emp.deptno)
                .select(emp -> emp.name)
                .reverse()
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Janet, Eric, Fred, Gates]", ss);

        Character[] lst = Linq.of(Arrays.asList('h', 'e', 'l', 'l', 'o')).reverse().toArray(Character.class);
        assertEquals(5, lst.length);
        assertEquals("h", lst[4].toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Character[] arr = Linq.of(arrChar).reverse().toArray(Character.class);
        assertEquals(5, arr.length);
        assertEquals("h", arr[4].toString());

        Character[] hello = Linq.chars("hello").reverse().toArray(Character.class);
        assertEquals(5, hello.length);
        assertEquals("h", hello[4].toString());

        Character[] h = Linq.singleton('h').reverse().toArray(Character.class);
        assertEquals(1, h.length);
        assertEquals("h", h[0].toString());

        Character[] empty = Linq.<Character>empty().reverse().toArray(Character.class);
        assertEquals(0, empty.length);

        assertEquals(Linq.empty(), Linq.ofNullable(null).reverse());
        assertEquals(Linq.singleton("hello"), Linq.ofNullable("hello").reverse());

        assertEquals(Linq.of("world", "hello"), Linq.of(Pattern.compile("[,!]").splitAsStream("hello,world!")).reverse());
        assertEquals(Linq.of("world", "hello"), Linq.of(Pattern.compile("[,!]").splitAsStream("hello,world!").spliterator()).reverse());

        assertEquals(Linq.of('\n', 'o', 'l', 'l', 'e', 'h'), Linq.chars("hello\n").reverse());
        assertEquals(Linq.of("world", "hello"), Linq.words(" hello,world! ").reverse());
        assertEquals(Linq.of("world", "hello"), Linq.lines("\r\nhello\r\nworld\r\n").reverse());

        assertThrows(OutOfMemoryError.class, () -> Linq.infinite(() -> 0).reverse().first());

        assertThrows(OutOfMemoryError.class, () -> Linq.loop(0, x -> x).reverse().first());
        assertEquals(99, Linq.loop(0, x -> x < 100, x -> ++x).reverse().first());

        IEnumerator<Integer> enumerator = new TestEnumerable<>(new Integer[]{1, 2}).enumerator();
        assertEquals(2, Linq.enumerate(enumerator::moveNext, enumerator::current).reverse().first());

        Iterator<Integer> iterator = new ArrayIterable<>(new Integer[]{1, 2}).iterator();
        assertEquals(2, Linq.iterate(iterator::hasNext, iterator::next).reverse().first());
    }
}
