package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.linq.util.HashSet;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class IntersectByTest extends TestCase {
    private static <TSource, TKey> Object[] WrapArgs(IEnumerable<TSource> first, IEnumerable<TKey> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer, IEnumerable<TSource> expected) {
        return new Object[]{first, second, keySelector, comparer, expected};
    }

    private static IEnumerable<Object[]> TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(WrapArgs(
                Linq.range(0, 10),
                Linq.range(0, 5),
                x -> x,
                null,
                Linq.range(0, 5)));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                Linq.range(10, 10),
                x -> x,
                null,
                Linq.empty()));

        argsList.add(WrapArgs(
                Linq.repeat(5, 20),
                Linq.empty(),
                x -> x,
                null,
                Linq.empty()));

        argsList.add(WrapArgs(
                Linq.repeat(5, 20),
                Linq.repeat(5, 3),
                x -> x,
                null,
                Linq.repeat(5, 1)));

        argsList.add(WrapArgs(
                Linq.of("Bob", "Tim", "Robert", "Chris"),
                Linq.of("bBo", "shriC"),
                x -> x,
                null,
                Linq.empty()));

        argsList.add(WrapArgs(
                Linq.of("Bob", "Tim", "Robert", "Chris"),
                Linq.of("bBo", "shriC"),
                x -> x,
                new AnagramEqualityComparer(),
                Linq.of("Bob", "Chris")));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40)),
                Linq.of(new int[]{15, 20, 40}),
                Tuple2::getItem2,
                null,
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Harry", 40))));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40)),
                Linq.of(new String[]{"moT"}),
                Tuple2::getItem2,
                null,
                Linq.empty()));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40)),
                Linq.of("moT"),
                Tuple2::getItem1,
                new AnagramEqualityComparer(),
                Linq.of(Tuple.create("Tom", 20))));

        return argsList;
    }

    private static IEnumerable<Object[]> Int_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[0]), Linq.of(new int[0]), new int[0]);
        argsList.add(Linq.of(new int[]{-5, 3, -2, 6, 9}), Linq.of(new int[]{0, 5, 2, 10, 20}), new int[0]);
        argsList.add(Linq.of(new int[]{1, 2, 2, 3, 4, 3, 5}), Linq.of(new int[]{1, 4, 4, 2, 2, 2}), new int[]{1, 2, 4});
        argsList.add(Linq.of(new int[]{1, 1, 1, 1, 1, 1}), Linq.of(new int[]{1, 1, 1, 1, 1}), new int[]{1});
        return argsList;
    }

    private static IEnumerable<Object[]> String_TestData() {
        ArgsList argsList = new ArgsList();
        IEqualityComparer<String> defaultComparer = EqualityComparer.Default();
        argsList.add(Linq.of(new String[1]), Linq.of(), defaultComparer, new String[0]);
        argsList.add(Linq.of(null, null, Empty), Linq.of(new String[2]), defaultComparer, new String[]{null});
        argsList.add(Linq.of(new String[2]), Linq.of(), defaultComparer, new String[0]);

        argsList.add(Linq.of("Tim", "Bob", "Mike", "Robert"), Linq.of("ekiM", "bBo"), null, new String[0]);
        argsList.add(Linq.of("Tim", "Bob", "Mike", "Robert"), Linq.of("ekiM", "bBo"), new AnagramEqualityComparer(), new String[]{"Bob", "Mike"});
        return argsList;
    }

    private static IEnumerable<Object[]> NullableInt_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(), Linq.of(-5, 0, null, 1, 2, 9, 2), new Integer[0]);
        argsList.add(Linq.of(-5, 0, 1, 2, null, 9, 2), Linq.of(), new Integer[0]);
        argsList.add(Linq.of(1, 2, null, 3, 4, 5, 6), Linq.of(6, 7, 7, 7, null, 8, 1), new Integer[]{1, null, 6});
        return argsList;
    }

    @Test
    void FirstNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = null;
        IEnumerable<String> second = Linq.of("bBo", "shriC");

        assertThrows(NullPointerException.class, () -> first.intersectBy(second, x -> x));
        assertThrows(NullPointerException.class, () -> first.intersectBy(second, x -> x, new AnagramEqualityComparer()));
    }

    @Test
    void SecondNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = Linq.of("Bob", "Tim", "Robert", "Chris");
        IEnumerable<String> second = null;

        assertThrows(ArgumentNullException.class, () -> first.intersectBy(second, x -> x));
        assertThrows(ArgumentNullException.class, () -> first.intersectBy(second, x -> x, new AnagramEqualityComparer()));
    }

    @Test
    void KeySelectorNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = Linq.of("Bob", "Tim", "Robert", "Chris");
        IEnumerable<String> second = Linq.of("bBo", "shriC");
        Func1<String, String> keySelector = null;

        assertThrows(ArgumentNullException.class, () -> first.intersectBy(second, keySelector));
        assertThrows(ArgumentNullException.class, () -> first.intersectBy(second, keySelector, new AnagramEqualityComparer()));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    <TSource, TKey> void HasExpectedOutput(IEnumerable<TSource> first, IEnumerable<TKey> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer, IEnumerable<TSource> expected) {
        assertEquals(expected, first.intersectBy(second, keySelector, comparer));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    <TSource, TKey> void RunOnce_HasExpectedOutput(IEnumerable<TSource> first, IEnumerable<TKey> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer, IEnumerable<TSource> expected) {
        assertEquals(expected, first.runOnce().intersectBy(second.runOnce(), keySelector, comparer));
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> first = Linq.of(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> second = Linq.of(1, 9, null, 4);

        assertEquals(first.intersectBy(second, x -> x), first.intersectBy(second, x -> x));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> first = Linq.of("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");
        IEnumerable<String> second = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS");

        assertEquals(first.intersectBy(second, x -> x), first.intersectBy(second, x -> x));
    }

    @ParameterizedTest
    @MethodSource("Int_TestData")
    void Int(IEnumerable<Integer> first, IEnumerable<Integer> second, int[] expected) {
        assertEquals(Linq.of(expected), first.intersectBy(second, x -> x));
        assertEquals(Linq.of(expected), first.intersectBy(second, x -> x, null));
    }

    @ParameterizedTest
    @MethodSource("String_TestData")
    void String(IEnumerable<String> first, IEnumerable<String> second, IEqualityComparer<String> comparer, String[] expected) {
        if (comparer == null) {
            assertEquals(Linq.of(expected), first.intersectBy(second, x -> x));
        }
        assertEquals(Linq.of(expected), first.intersectBy(second, x -> x, comparer));
    }

    @ParameterizedTest
    @MethodSource("NullableInt_TestData")
    void NullableInt(IEnumerable<Integer> first, IEnumerable<Integer> second, Integer[] expected) {
        assertEquals(Linq.of(expected), first.intersectBy(second, x -> x));
        assertEquals(Linq.of(expected), first.intersectBy(second, x -> x, null));
    }

    @ParameterizedTest
    @MethodSource("NullableInt_TestData")
    void NullableIntRunOnce(IEnumerable<Integer> first, IEnumerable<Integer> second, Integer[] expected) {
        assertEquals(Linq.of(expected), first.runOnce().intersectBy(second.runOnce(), x -> x));
        assertEquals(Linq.of(expected), first.runOnce().intersectBy(second.runOnce(), x -> x, null));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).intersectBy(Linq.range(0, 3), x -> x);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void HashSetWithBuiltInComparer_HashSetContainsNotUsed() {
        HashSet<String> set = new HashSet<>(StringComparer.OrdinalIgnoreCase);
        set.add("a");

        IEnumerable<String> input1 = Linq.of(set);
        IEnumerable<String> input2 = Linq.of("A");

        assertEquals(Linq.empty(), input1.intersectBy(input2, x -> x));
        assertEquals(Linq.empty(), input1.intersectBy(input2, x -> x, null));
        assertEquals(Linq.empty(), input1.intersectBy(input2, x -> x, EqualityComparer.Default()));
        assertEquals(Linq.of("a"), input1.intersectBy(input2, x -> x, StringComparer.OrdinalIgnoreCase));

        assertEquals(Linq.empty(), input2.intersectBy(input1, x -> x));
        assertEquals(Linq.empty(), input2.intersectBy(input1, x -> x, null));
        assertEquals(Linq.empty(), input2.intersectBy(input1, x -> x, EqualityComparer.Default()));
        assertEquals(Linq.of("A"), input2.intersectBy(input1, x -> x, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    void testIntersectBy() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 30),
                emps[3],
        };
        assertEquals(2, Linq.of(emps)
                .intersectBy(Linq.of(emps2).select(e -> e.deptno), emp -> emp.deptno)
                .count());

        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).intersectBy(Linq.empty(), x -> x));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2, 3).intersectBy(null, x -> x));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2, 3).intersectBy(Linq.empty(), null));

        IEnumerable<Integer> source = Linq.of(1, 2, 3).intersectBy(Linq.singleton(1), x -> x);
        IEnumerator<Integer> e1 = source.enumerator();
        assertTrue(e1.moveNext());
        IEnumerator<Integer> e2 = source.enumerator();
        assertTrue(e2.moveNext());
        assertNotSame(e1, e2);
    }

    @Test
    void testIntersectByWithComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };

        Employee[] emps2 = {
                new Employee(150, "Theodore", 10)
        };
        assertEquals(1, Linq.of(emps)
                .intersectBy(Linq.of(emps2).select(e -> e.deptno), emp -> emp.deptno, comparer)
                .count());
    }
}
