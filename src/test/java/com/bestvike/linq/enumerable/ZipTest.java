package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class ZipTest extends TestCase {
    @Test
    void ImplicitTypeParameters() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 5, 9});
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 7, 12});

        assertEquals(expected, first.zip(second, (x, y) -> x + y));
    }

    @Test
    void ExplicitTypeParameters() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 5, 9});
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 7, 12});

        assertEquals(expected, first.zip(second, (x, y) -> x + y));
    }

    @Test
    void FirstIsNull() {
        IEnumerable<Integer> first = null;
        IEnumerable<Integer> second = Linq.of(new int[]{2, 5, 9});

        assertThrows(NullPointerException.class, () -> first.zip(second, (x, y) -> x + y));
    }

    @Test
    void SecondIsNull() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = null;

        assertThrows(ArgumentNullException.class, () -> first.zip(second, (x, y) -> x + y));
    }

    @Test
    void FuncIsNull() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6});
        Func2<Integer, Integer, Integer> func = null;

        assertThrows(ArgumentNullException.class, () -> first.zip(second, func));
    }

    @Test
    void ExceptionThrownFromFirstsEnumerator() {
        ThrowsOnMatchEnumerable<Integer> first = new ThrowsOnMatchEnumerable<>(Linq.of(new int[]{1, 3, 3}), 2);
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 7, 9});

        assertEquals(expected, first.zip(second, func));

        first = new ThrowsOnMatchEnumerable<>(Linq.of(new int[]{1, 2, 3}), 2);

        IEnumerable<Integer> zip = first.zip(second, func);

        assertThrows(RuntimeException.class, () -> zip.toList());
    }

    @Test
    void ExceptionThrownFromSecondsEnumerator() {
        ThrowsOnMatchEnumerable<Integer> second = new ThrowsOnMatchEnumerable<>(Linq.of(new int[]{1, 3, 3}), 2);
        IEnumerable<Integer> first = Linq.of(new int[]{2, 4, 6});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 7, 9});

        assertEquals(expected, first.zip(second, func));

        second = new ThrowsOnMatchEnumerable<>(Linq.of(new int[]{1, 2, 3}), 2);

        IEnumerable<Integer> zip = first.zip(second, func);

        assertThrows(RuntimeException.class, () -> zip.toList());
    }

    @Test
    void FirstAndSecondEmpty() {
        IEnumerable<Integer> first = Linq.of(new int[]{});
        IEnumerable<Integer> second = Linq.of(new int[]{});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstEmptySecondSingle() {
        IEnumerable<Integer> first = Linq.of(new int[]{});
        IEnumerable<Integer> second = Linq.of(new int[]{2});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstEmptySecondMany() {
        IEnumerable<Integer> first = Linq.of(new int[]{});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondEmptyFirstSingle() {
        IEnumerable<Integer> first = Linq.of(new int[]{1});
        IEnumerable<Integer> second = Linq.of(new int[]{});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondEmptyFirstMany() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstAndSecondSingle() {
        IEnumerable<Integer> first = Linq.of(new int[]{1});
        IEnumerable<Integer> second = Linq.of(new int[]{2});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstAndSecondEqualSize() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 3, 4});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 5, 7});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondOneMoreThanFirst() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 6});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondManyMoreThanFirst() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8, 16});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 6});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstOneMoreThanSecond() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 6});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstManyMoreThanSecond() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 6});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void DelegateFuncChanged() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 6, 11});

        assertEquals(expected, first.zip(second, func));

        func = (x, y) -> x - y;
        expected = Linq.of(new int[]{-1, -2, -5});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void LambdaFuncChanged() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8});
        IEnumerable<Integer> expected = Linq.of(new int[]{3, 6, 11});

        assertEquals(expected, first.zip(second, (x, y) -> x + y));

        expected = Linq.of(new int[]{-1, -2, -5});

        assertEquals(expected, first.zip(second, (x, y) -> x - y));
    }

    @Test
    void FirstHasFirstElementNull() {
        IEnumerable<Integer> first = Linq.of(null, 2, 3, 4);
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(null, 6, 11);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstHasLastElementNull() {
        IEnumerable<Integer> first = Linq.of(1, 2, null);
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(3, 6, null);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstHasMiddleNullValue() {
        IEnumerable<Integer> first = Linq.of(1, null, 3);
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(3, null, 9);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstAllElementsNull() {
        IEnumerable<Integer> first = Linq.of(new Integer[]{null, null, null});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondHasFirstElementNull() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.of(null, 4, 6);
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(null, 6, 9);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondHasLastElementNull() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.of(2, 4, null);
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(3, 6, null);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondHasMiddleElementNull() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.of(2, null, 6);
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(3, null, 9);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondHasAllElementsNull() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.of(new Integer[]{null, null, null});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void SecondLargerFirstAllNull() {
        IEnumerable<Integer> first = Linq.of(new Integer[]{null, null, null, null});
        IEnumerable<Integer> second = Linq.of(new Integer[]{null, null, null});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstSameSizeSecondAllNull() {
        IEnumerable<Integer> first = Linq.of(new Integer[]{null, null, null});
        IEnumerable<Integer> second = Linq.of(new Integer[]{null, null, null});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void FirstSmallerSecondAllNull() {
        IEnumerable<Integer> first = Linq.of(new Integer[]{null, null, null});
        IEnumerable<Integer> second = Linq.of(new Integer[]{null, null, null, null});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).zip(Linq.range(0, 3), (x, y) -> x + y);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void RunOnce() {
        IEnumerable<Integer> first = Linq.of(1, null, 3);
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.of(3, null, 9);

        assertEquals(expected, first.runOnce().zip(second.runOnce(), func));
    }

    @Test
    void Zip2_ImplicitTypeParameters() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 5, 9});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(2, 5), Tuple.create(3, 9));

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_ExplicitTypeParameters() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 5, 9});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(2, 5), Tuple.create(3, 9));

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_FirstIsNull() {
        IEnumerable<Integer> first = null;
        IEnumerable<Integer> second = Linq.of(new int[]{2, 5, 9});

        assertThrows(NullPointerException.class, () -> first.zip(second));
    }

    @Test
    void Zip2_SecondIsNull() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = null;

        assertThrows(ArgumentNullException.class, () -> first.zip(second));
    }

    @Test
    void Zip2_ExceptionThrownFromFirstsEnumerator() {
        ThrowsOnMatchEnumerable<Integer> first = new ThrowsOnMatchEnumerable<>(Linq.of(new int[]{1, 3, 3}), 2);
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(3, 4), Tuple.create(3, 6));

        assertEquals(expected, first.zip(second));

        first = new ThrowsOnMatchEnumerable<>(Linq.of(new int[]{1, 2, 3}), 2);

        IEnumerable<Tuple2<Integer, Integer>> zip = first.zip(second);

        assertThrows(RuntimeException.class, () -> zip.toList());
    }

    @Test
    void Zip2_ExceptionThrownFromSecondsEnumerator() {
        ThrowsOnMatchEnumerable<Integer> second = new ThrowsOnMatchEnumerable<>(Linq.of(new int[]{1, 3, 3}), 2);
        IEnumerable<Integer> first = Linq.of(new int[]{2, 4, 6});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(2, 1), Tuple.create(4, 3), Tuple.create(6, 3));

        assertEquals(expected, first.zip(second));

        second = new ThrowsOnMatchEnumerable<>(Linq.of(new int[]{1, 2, 3}), 2);

        IEnumerable<Tuple2<Integer, Integer>> zip = first.zip(second);

        assertThrows(RuntimeException.class, () -> zip.toList());
    }

    @Test
    void Zip2_FirstAndSecondEmpty() {
        IEnumerable<Integer> first = Linq.of(new int[]{});
        IEnumerable<Integer> second = Linq.of(new int[]{});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_FirstEmptySecondSingle() {
        IEnumerable<Integer> first = Linq.of(new int[]{});
        IEnumerable<Integer> second = Linq.of(new int[]{2});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_FirstEmptySecondMany() {
        IEnumerable<Integer> first = Linq.of(new int[]{});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_SecondEmptyFirstSingle() {
        IEnumerable<Integer> first = Linq.of(new int[]{1});
        IEnumerable<Integer> second = Linq.of(new int[]{});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_SecondEmptyFirstMany() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_FirstAndSecondSingle() {
        IEnumerable<Integer> first = Linq.of(new int[]{1});
        IEnumerable<Integer> second = Linq.of(new int[]{2});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2));

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_FirstAndSecondEqualSize() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 3, 4});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(2, 3), Tuple.create(3, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_SecondOneMoreThanFirst() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(2, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_SecondManyMoreThanFirst() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 8, 16});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(2, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_FirstOneMoreThanSecond() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(2, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_FirstManyMoreThanSecond() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(2, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    void Zip2_RunOnce() {
        IEnumerable<Integer> first = Linq.of(1, null, 3);
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6, 8});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.of(Tuple.create(1, 2), Tuple.create(null, 4), Tuple.create(3, 6));

        assertEquals(expected, first.runOnce().zip(second.runOnce()));
    }

    @Test
    void Zip2_NestedTuple() {
        IEnumerable<Integer> first = Linq.of(new int[]{1, 3, 5});
        IEnumerable<Integer> second = Linq.of(new int[]{2, 4, 6});
        IEnumerable<Tuple2<Integer, Integer>> third = Linq.of(Tuple.create(1, 2), Tuple.create(3, 4), Tuple.create(5, 6));

        assertEquals(third, first.zip(second));

        IEnumerable<String> fourth = Linq.of("one", "two", "three");

        IEnumerable<Tuple2<Tuple2<Integer, Integer>, String>> final2 = Linq.of(Tuple.create(Tuple.create(1, 2), "one"), Tuple.create(Tuple.create(3, 4), "two"), Tuple.create(Tuple.create(5, 6), "three"));
        assertEquals(final2, third.zip(fourth));
    }

    @Test
    void Zip2_TupleNames() {
        Tuple2<Integer, Integer> t = Linq.of(new int[]{1, 2, 3}).zip(Linq.of(new int[]{2, 4, 6})).first();
        assertEquals(1, t.getItem1());
        assertEquals(2, t.getItem2());
    }

    @Test
    void testZip() {
        IEnumerable<String> e1 = Linq.of(Arrays.asList("a", "b", "c"));
        IEnumerable<String> e2 = Linq.of(Arrays.asList("1", "2", "3"));

        IEnumerable<String> zipped = e1.zip(e2, (v0, v1) -> v0 + v1);
        assertEquals(3, zipped.count());
        for (int i = 0; i < 3; i++)
            assertEquals(Empty + (char) ('a' + i) + (char) ('1' + i), zipped.elementAt(i));

        IEnumerable<Tuple2<String, String>> zipped2 = e1.zip(e2);
        assertEquals(3, zipped2.count());
        for (int i = 0; i < 3; i++) {
            assertEquals(Empty + (char) ('a' + i), zipped2.elementAt(i).getItem1());
            assertEquals(Empty + (char) ('1' + i), zipped2.elementAt(i).getItem2());
        }

        IEnumerable<Tuple2<String, String>> source = e1.zip(e2);
        try (IEnumerator<Tuple2<String, String>> e = source.enumerator()) {
            for (int i = 0; i < 3; i++)
                assertTrue(e.moveNext());
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    void testZipLengthNotMatch() {
        IEnumerable<String> e1 = Linq.of(Arrays.asList("a", "b"));
        IEnumerable<String> e2 = Linq.of(Arrays.asList("1", "2", "3"));

        IEnumerable<String> zipped1 = e1.zip(e2, (v0, v1) -> v0 + v1);
        assertEquals(2, zipped1.count());
        for (int i = 0; i < 2; i++)
            assertEquals(Empty + (char) ('a' + i) + (char) ('1' + i), zipped1.elementAt(i));

        IEnumerable<String> zipped2 = e2.zip(e1, (v0, v1) -> v0 + v1);
        assertEquals(2, zipped2.count());
        for (int i = 0; i < 2; i++)
            assertEquals(Empty + (char) ('1' + i) + (char) ('a' + i), zipped2.elementAt(i));

        IEnumerable<Tuple2<String, String>> zipped3 = e1.zip(e2);
        assertEquals(2, zipped3.count());
        for (int i = 0; i < 2; i++) {
            assertEquals(Empty + (char) ('a' + i), zipped3.elementAt(i).getItem1());
            assertEquals(Empty + (char) ('1' + i), zipped3.elementAt(i).getItem2());
        }

        IEnumerable<Tuple2<String, String>> zipped4 = e2.zip(e1);
        assertEquals(2, zipped4.count());
        for (int i = 0; i < 2; i++) {
            assertEquals(Empty + (char) ('1' + i), zipped4.elementAt(i).getItem1());
            assertEquals(Empty + (char) ('a' + i), zipped4.elementAt(i).getItem2());
        }
    }
}
