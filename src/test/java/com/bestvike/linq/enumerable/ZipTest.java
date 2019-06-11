package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ZipTest extends TestCase {
    @Test
    public void ImplicitTypeParameters() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 5, 9});
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 7, 12});

        assertEquals(expected, first.zip(second, (x, y) -> x + y));
    }

    @Test
    public void ExplicitTypeParameters() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 5, 9});
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 7, 12});

        assertEquals(expected, first.zip(second, (x, y) -> x + y));
    }

    @Test
    public void FirstIsNull() {
        IEnumerable<Integer> first = null;
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 5, 9});

        assertThrows(NullPointerException.class, () -> first.zip(second, (x, y) -> x + y));
    }

    @Test
    public void SecondIsNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = null;

        assertThrows(ArgumentNullException.class, () -> first.zip(second, (x, y) -> x + y));
    }

    @Test
    public void FuncIsNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6});
        Func2<Integer, Integer, Integer> func = null;

        assertThrows(ArgumentNullException.class, () -> first.zip(second, func));
    }

    @Test
    public void ExceptionThrownFromFirstsEnumerator() {
        ThrowsOnMatchEnumerable<Integer> first = new ThrowsOnMatchEnumerable<>(Linq.asEnumerable(new int[]{1, 3, 3}), 2);
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 7, 9});

        assertEquals(expected, first.zip(second, func));

        first = new ThrowsOnMatchEnumerable<>(Linq.asEnumerable(new int[]{1, 2, 3}), 2);

        IEnumerable<Integer> zip = first.zip(second, func);

        assertThrows(Exception.class, () -> zip.toList());
    }

    @Test
    public void ExceptionThrownFromSecondsEnumerator() {
        ThrowsOnMatchEnumerable<Integer> second = new ThrowsOnMatchEnumerable<>(Linq.asEnumerable(new int[]{1, 3, 3}), 2);
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{2, 4, 6});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 7, 9});

        assertEquals(expected, first.zip(second, func));

        second = new ThrowsOnMatchEnumerable<>(Linq.asEnumerable(new int[]{1, 2, 3}), 2);

        IEnumerable<Integer> zip = first.zip(second, func);

        assertThrows(Exception.class, () -> zip.toList());
    }

    @Test
    public void FirstAndSecondEmpty() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstEmptySecondSingle() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstEmptySecondMany() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondEmptyFirstSingle() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondEmptyFirstMany() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstAndSecondSingle() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstAndSecondEqualSize() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 3, 4});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 5, 7});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondOneMoreThanFirst() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 6});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondManyMoreThanFirst() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8, 16});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 6});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstOneMoreThanSecond() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 6});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstManyMoreThanSecond() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 6});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void DelegateFuncChanged() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 6, 11});

        assertEquals(expected, first.zip(second, func));

        func = (x, y) -> x - y;
        expected = Linq.asEnumerable(new int[]{-1, -2, -5});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void LambdaFuncChanged() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8});
        IEnumerable<Integer> expected = Linq.asEnumerable(new int[]{3, 6, 11});

        assertEquals(expected, first.zip(second, (x, y) -> x + y));

        expected = Linq.asEnumerable(new int[]{-1, -2, -5});

        assertEquals(expected, first.zip(second, (x, y) -> x - y));
    }

    @Test
    public void FirstHasFirstElementNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(null, 2, 3, 4);
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(null, 6, 11);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstHasLastElementNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(1, 2, null);
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(3, 6, null);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstHasMiddleNullValue() {
        IEnumerable<Integer> first = Linq.asEnumerable(1, null, 3);
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(3, null, 9);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstAllElementsNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new Integer[]{null, null, null});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondHasFirstElementNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.asEnumerable(null, 4, 6);
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(null, 6, 9);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondHasLastElementNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.asEnumerable(2, 4, null);
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(3, 6, null);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondHasMiddleElementNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.asEnumerable(2, null, 6);
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(3, null, 9);

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondHasAllElementsNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.asEnumerable(new Integer[]{null, null, null});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void SecondLargerFirstAllNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new Integer[]{null, null, null, null});
        IEnumerable<Integer> second = Linq.asEnumerable(new Integer[]{null, null, null});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstSameSizeSecondAllNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new Integer[]{null, null, null});
        IEnumerable<Integer> second = Linq.asEnumerable(new Integer[]{null, null, null});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void FirstSmallerSecondAllNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new Integer[]{null, null, null});
        IEnumerable<Integer> second = Linq.asEnumerable(new Integer[]{null, null, null, null});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(new Integer[]{null, null, null});

        assertEquals(expected, first.zip(second, func));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).zip(Linq.range(0, 3), (x, y) -> x + y);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void RunOnce() {
        IEnumerable<Integer> first = Linq.asEnumerable(1, null, 3);
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6, 8});
        Func2<Integer, Integer, Integer> func = (x, y) -> x == null || y == null ? null : x + y;
        IEnumerable<Integer> expected = Linq.asEnumerable(3, null, 9);

        assertEquals(expected, first.runOnce().zip(second.runOnce(), func));
    }

    @Test
    public void Zip2_ImplicitTypeParameters() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 5, 9});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(2, 5), Tuple.create(3, 9));

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_ExplicitTypeParameters() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 5, 9});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(2, 5), Tuple.create(3, 9));

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_FirstIsNull() {
        IEnumerable<Integer> first = null;
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 5, 9});

        assertThrows(NullPointerException.class, () -> first.zip(second));
    }

    @Test
    public void Zip2_SecondIsNull() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = null;

        assertThrows(ArgumentNullException.class, () -> first.zip(second));
    }

    @Test
    public void Zip2_ExceptionThrownFromFirstsEnumerator() {
        ThrowsOnMatchEnumerable<Integer> first = new ThrowsOnMatchEnumerable<>(Linq.asEnumerable(new int[]{1, 3, 3}), 2);
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(3, 4), Tuple.create(3, 6));

        assertEquals(expected, first.zip(second));

        first = new ThrowsOnMatchEnumerable<>(Linq.asEnumerable(new int[]{1, 2, 3}), 2);

        IEnumerable<Tuple2<Integer, Integer>> zip = first.zip(second);

        assertThrows(Exception.class, () -> zip.toList());
    }

    @Test
    public void Zip2_ExceptionThrownFromSecondsEnumerator() {
        ThrowsOnMatchEnumerable<Integer> second = new ThrowsOnMatchEnumerable<>(Linq.asEnumerable(new int[]{1, 3, 3}), 2);
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{2, 4, 6});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(2, 1), Tuple.create(4, 3), Tuple.create(6, 3));

        assertEquals(expected, first.zip(second));

        second = new ThrowsOnMatchEnumerable<>(Linq.asEnumerable(new int[]{1, 2, 3}), 2);

        IEnumerable<Tuple2<Integer, Integer>> zip = first.zip(second);

        assertThrows(Exception.class, () -> zip.toList());
    }

    @Test
    public void Zip2_FirstAndSecondEmpty() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_FirstEmptySecondSingle() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_FirstEmptySecondMany() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_SecondEmptyFirstSingle() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_SecondEmptyFirstMany() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.empty();

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_FirstAndSecondSingle() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2));

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_FirstAndSecondEqualSize() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 3, 4});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(2, 3), Tuple.create(3, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_SecondOneMoreThanFirst() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(2, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_SecondManyMoreThanFirst() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 8, 16});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(2, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_FirstOneMoreThanSecond() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(2, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_FirstManyMoreThanSecond() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 2, 3, 4});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(2, 4));

        assertEquals(expected, first.zip(second));
    }

    @Test
    public void Zip2_RunOnce() {
        IEnumerable<Integer> first = Linq.asEnumerable(1, null, 3);
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6, 8});
        IEnumerable<Tuple2<Integer, Integer>> expected = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(null, 4), Tuple.create(3, 6));

        assertEquals(expected, first.runOnce().zip(second.runOnce()));
    }

    @Test
    public void Zip2_NestedTuple() {
        IEnumerable<Integer> first = Linq.asEnumerable(new int[]{1, 3, 5});
        IEnumerable<Integer> second = Linq.asEnumerable(new int[]{2, 4, 6});
        IEnumerable<Tuple2<Integer, Integer>> third = Linq.asEnumerable(Tuple.create(1, 2), Tuple.create(3, 4), Tuple.create(5, 6));

        assertEquals(third, first.zip(second));

        IEnumerable<String> fourth = Linq.asEnumerable("one", "two", "three");

        IEnumerable<Tuple2<Tuple2<Integer, Integer>, String>> final2 = Linq.asEnumerable(Tuple.create(Tuple.create(1, 2), "one"), Tuple.create(Tuple.create(3, 4), "two"), Tuple.create(Tuple.create(5, 6), "three"));
        assertEquals(final2, third.zip(fourth));
    }

    @Test
    public void Zip2_TupleNames() {
        Tuple2<Integer, Integer> t = Linq.asEnumerable(new int[]{1, 2, 3}).zip(Linq.asEnumerable(new int[]{2, 4, 6})).first();
        assertEquals(1, t.getItem1());
        assertEquals(2, t.getItem2());
    }

    @Test
    public void testZip() {
        IEnumerable<String> e1 = Linq.asEnumerable(Arrays.asList("a", "b", "c"));
        IEnumerable<String> e2 = Linq.asEnumerable(Arrays.asList("1", "2", "3"));

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
    }

    @Test
    public void testZipLengthNotMatch() {
        IEnumerable<String> e1 = Linq.asEnumerable(Arrays.asList("a", "b"));
        IEnumerable<String> e2 = Linq.asEnumerable(Arrays.asList("1", "2", "3"));

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
